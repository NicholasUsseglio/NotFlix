package com.notflix.springVideo.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.notflix.springVideo.dto.FilmDTO;
import com.notflix.springVideo.entities.Valutazione;
import com.notflix.springVideo.services.FilmService;
import com.notflix.springVideo.services.PreferitoService;
import com.notflix.springVideo.services.ValutazioneService;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/film")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @Autowired
    private ValutazioneService valutazioneService;

    @Autowired
    private PreferitoService preferitoService;

    @Autowired
    private com.notflix.springVideo.services.WatchlistService watchlistService;

    @Autowired
    private com.notflix.springVideo.services.UtenteService utenteService;

    @Autowired
    private com.notflix.springVideo.services.AttoreService attoreService;

    // -----------------------
    // PAGINA DETTAGLIO FILM
    // -----------------------
    @GetMapping("/{id}")
    public String getSchedaFilm(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {

        List<FilmDTO> filmCorrelati = Collections.emptyList();
        // DTO base
        Optional<FilmDTO> maybeFilm = filmService.findDtoByIdSafe(id);
        if (maybeFilm.isEmpty()) {
            return "film-non-trovato";
        }
        FilmDTO film = maybeFilm.get();
        model.addAttribute("film", film);

        // Entity completa
        Optional<com.notflix.springVideo.entities.Film> maybeEntity = filmService.findByIdSafe(id);
        if (maybeEntity.isPresent()) {
            var entity = maybeEntity.get();

            // cast con fallback per dati seed
            var cast = entity.getAttori();
            if ((cast == null || cast.isEmpty()) && film.getTitolo() != null) {
                try {
                    var fromFilm = attoreService.getByFilm(film.getTitolo());
                    if (fromFilm != null && !fromFilm.isEmpty()) {
                        cast = fromFilm;
                    } else {
                        var fromSerie = attoreService.getBySerieTv(film.getTitolo());
                        if (fromSerie != null && !fromSerie.isEmpty()) {
                            cast = fromSerie;
                        }
                    }
                } catch (Exception ignored) {}
            }
            model.addAttribute("cast", cast != null ? cast : java.util.Collections.emptyList());

            // regista: preferisci entity se DTO nullo
            if (film.getRegista() == null && entity.getRegista() != null) {
                film.setRegista(entity.getRegista());
                model.addAttribute("film", film);
            }
        } else {
            model.addAttribute("cast", java.util.Collections.emptyList());
        }

        // regista e nome sicuro
        com.notflix.springVideo.entities.Regista reg = film.getRegista();
        if (reg == null && maybeEntity.isPresent()) {
            reg = maybeEntity.get().getRegista();
        }
        model.addAttribute("regista", reg);

        String registaNome = "N/D";
        try {
            if (reg != null) {
                String n = reg.getNome();
                String c = reg.getCognome();
                if ((n != null && !n.isBlank()) || (c != null && !c.isBlank())) {
                    registaNome = ((n != null) ? n : "") + " " + ((c != null) ? c : "");
                    registaNome = registaNome.trim();
                }
            }
        } catch (Exception ignored) {}
        if ("N/D".equals(registaNome)) {
            try {
                String dbName = filmService.getDirectorNameFromDb(id);
                if (dbName != null && !dbName.isBlank()) registaNome = dbName;
            } catch (Exception ignored) {}
        }
        model.addAttribute("registaNome", registaNome);

        // rating aggregato
        Double media = valutazioneService.getMedia(id);
        long numRecensioni = valutazioneService.getNumeroRecensioni(id);
        model.addAttribute("mediaVoto", media);
        model.addAttribute("numRecensioni", numRecensioni);

        // recensioni pubbliche
        List<Valutazione> recensioni = valutazioneService.getRecensioni(id);
        model.addAttribute("recensioni", recensioni);

        // utente loggato
        Long idUtente = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            try {
                var dto = utenteService.getByUsername(auth.getName());
                if (dto != null) idUtente = dto.getId();
            } catch (Exception ignored) {}
        }
        model.addAttribute("idUtente", idUtente);

        // default per Thymeleaf
        model.addAttribute("isInWatchlist", false);

        // preferito, watchlist, valutazione personale
        if (idUtente != null) {
            boolean preferito = preferitoService.isPreferito(idUtente, id);
            model.addAttribute("isPreferito", preferito);

            boolean inWatchlist = watchlistService.isInWatchlist(idUtente, id);
            model.addAttribute("isInWatchlist", inWatchlist);

            Optional<Valutazione> miaVal = valutazioneService.getValutazioneUtente(idUtente, id);
            if (miaVal.isPresent()) {
                model.addAttribute("mioVoto", miaVal.get().getVoto());
                model.addAttribute("miaRecensione", miaVal.get().getRecensione());
            } else {
                model.addAttribute("mioVoto", 0);
                model.addAttribute("miaRecensione", "");
            }
        }
            // -----------------------
        // LOGICA FILM CORRELATI: AGGIUNTA DOPO CHE TUTTI I DATI SONO STATI CARICATI
        // -----------------------
        if (maybeFilm.isPresent()) {
            com.notflix.springVideo.dto.FilmDTO currentFilm = maybeFilm.get();
            String genere = currentFilm.getGenere();
            Long currentId = currentFilm.getId();
            
            // Se il genere è valido, prova a recuperare i correlati
            if (genere != null && !genere.isBlank() && currentId != null) {
                try {
                    // Chiama il metodo del Service
                    filmCorrelati = filmService.findTop3CorrelatiByGenere(currentId, genere);
                    
                } catch (Exception e) {
                    System.err.println("Errore nel recupero dei film correlati: " + e.getMessage());
                }
            }
        }
        
        // Aggiunge la lista (anche se vuota) al modello per Thymeleaf
        model.addAttribute("filmCorrelatiPerGenere", filmCorrelati);
        // -----------------------

        return "scheda-film";
    }

    // -----------------------
    // SALVA / AGGIORNA VALUTAZIONE
    // -----------------------
    @PostMapping("/{id}/valuta")
    public String salvaValutazione(
            @PathVariable Long id,
            @RequestParam("voto") String votoRaw,
            @RequestParam("recensione") String recensione,
            HttpSession session
    ) {
        Long idUtente = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var dto = utenteService.getByUsername(auth.getName());
            if (dto != null) idUtente = dto.getId();
        }
        if (idUtente == null) {
            return "redirect:/auth/login";
        }

        Double voto = 1.0;
        if (votoRaw != null && !votoRaw.isBlank()) {
            try {
                String normalized = votoRaw.replace(',', '.');
                voto = Double.parseDouble(normalized);
            } catch (NumberFormatException ignored) { voto = 1.0; }
        }
        if (voto.isNaN() || voto.isInfinite()) voto = 1.0;
        if (voto < 1.0) voto = 1.0;
        if (voto > 5.0) voto = 5.0;

        valutazioneService.salvaValutazione(idUtente, id, voto, recensione);

        return "redirect:/film/" + id;
    }

    // -----------------------
    // GESTIONE PREFERITI
    // -----------------------
    @PostMapping("/{id}/preferito")
    public String togglePreferito(
            @PathVariable Long id,
            HttpSession session
    ) {
        Long idUtente = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var dto = utenteService.getByUsername(auth.getName());
            if (dto != null) idUtente = dto.getId();
        }
        if (idUtente == null) {
            return "redirect:/auth/login";
        }

        preferitoService.togglePreferito(idUtente, id);

        return "redirect:/film/" + id;
    }

    // -----------------------
    // WATCHLIST
    // -----------------------
    @PostMapping("/{id}/watchlist")
    public String toggleWatchlist(
            @PathVariable Long id,
            HttpSession session
    ) {
        Long idUtente = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            var dto = utenteService.getByUsername(auth.getName());
            if (dto != null) idUtente = dto.getId();
        }
        if (idUtente == null) {
            return "redirect:/auth/login";
        }

        watchlistService.toggleWatchlist(idUtente, id);

        return "redirect:/film/" + id;
    }
}

