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

import com.notflix.springVideo.dto.SerieTvDTO;
import com.notflix.springVideo.entities.Valutazione;
import com.notflix.springVideo.services.SerieTvService;
import com.notflix.springVideo.services.PreferitoService;
import com.notflix.springVideo.services.ValutazioneService;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/serietv")
public class SerieTvController {

    @Autowired
    private SerieTvService serietvService;

    @Autowired
    private ValutazioneService valutazioneService;

    @Autowired
    private PreferitoService preferitoService;

    @Autowired
    private com.notflix.springVideo.services.WatchlistService watchlistService;

    @Autowired
    private com.notflix.springVideo.services.UtenteService utenteService;

    // -----------------------
    // PAGINA DETTAGLIO SERIE TV
    // -----------------------
    @GetMapping("/{id}")
    public String getSchedaSerietv(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {
        // Serie TV base (DTO)
        Optional<SerieTvDTO> maybeSerietv = serietvService.findDtoByIdSafe(id);
        if (maybeSerietv.isEmpty()) {
            return "serietv-non-trovata"; // crea un template semplice
        }
        SerieTvDTO serietv = maybeSerietv.get();
        System.out.println("===== DEBUG SERIE TV =====");
        System.out.println("ID: " + serietv.getId());
        System.out.println("Titolo: " + serietv.getTitolo());
        System.out.println("Trama: " + serietv.getTrama());
        System.out.println("Episodi: " + serietv.getEpisodi());
        System.out.println("Numero Stagioni: " + serietv.getNumeroStagioni());
        System.out.println("Numero Episodi: " + serietv.getNumeroEpisodi());
        System.out.println("==========================");

        model.addAttribute("serietv", serietv);

        // Entity completa (cast, regista, ecc.)
        Optional<com.notflix.springVideo.entities.SerieTV> maybeEntity = serietvService.findByIdSafe(id);
        if (maybeEntity.isPresent()) {
            var entity = maybeEntity.get();
            model.addAttribute("cast", entity.getAttori());
            if (serietv.getRegista() == null && entity.getRegista() != null) {
                serietv.setRegista(entity.getRegista());
                model.addAttribute("serietv", serietv);
            }
        } else {
            model.addAttribute("cast", java.util.Collections.emptyList());
        }

        // Regista (nome completo)
        com.notflix.springVideo.entities.Regista reg = serietv.getRegista();
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
        } catch (Exception ex) {
            // ignore
        }

        if ("N/D".equals(registaNome)) {
            try {
                String dbName = serietvService.getDirectorNameFromDb(id);
                if (dbName != null && !dbName.isBlank()) {
                    registaNome = dbName;
                }
            } catch (Exception ignored) {}
        }
        model.addAttribute("registaNome", registaNome);

        // Rating aggregato
        Double media = valutazioneService.getMedia(id);
        long numRecensioni = valutazioneService.getNumeroRecensioni(id);
        model.addAttribute("mediaVoto", media);
        model.addAttribute("numRecensioni", numRecensioni);

        // Recensioni pubbliche
        List<Valutazione> recensioni = valutazioneService.getRecensioni(id);
        model.addAttribute("recensioni", recensioni);

        // Utente loggato
        Long idUtente = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            try {
                var dto = utenteService.getByUsername(auth.getName());
                if (dto != null) idUtente = dto.getId();
            } catch (Exception ignored) {}
        }
        model.addAttribute("idUtente", idUtente);

    // Default (evita errori Thymeleaf)
    model.addAttribute("isWatchlist", false);

        // Se l'utente è loggato: preferiti e valutazioni personali
        if (idUtente != null) {
            boolean preferito = preferitoService.isPreferito(idUtente, id);
            model.addAttribute("isPreferito", preferito);

            boolean inWatchlist = watchlistService.isInWatchlist(idUtente, id);
            model.addAttribute("isWatchlist", inWatchlist);

            Optional<Valutazione> miaVal =
                    valutazioneService.getValutazioneUtente(idUtente, id);

            if (miaVal.isPresent()) {
                model.addAttribute("mioVoto", miaVal.get().getVoto());
                model.addAttribute("miaRecensione", miaVal.get().getRecensione());
            } else {
                model.addAttribute("mioVoto", 0);
                model.addAttribute("miaRecensione", "");
            }
        }

        // -----------------------
        // LOGICA SERIE TV CORRELATE
        // -----------------------
        List<SerieTvDTO> serieTvCorrelate = Collections.emptyList();
        if (maybeSerietv.isPresent()) {
            SerieTvDTO currentSerietv = maybeSerietv.get();
            String genere = currentSerietv.getGenere();
            Long currentId = currentSerietv.getId();
            
            // Se il genere è valido, prova a recuperare i correlati
            if (genere != null && !genere.isBlank() && currentId != null) {
                try {
                    // Chiama il metodo del Service (che DEVI implementare)
                    serieTvCorrelate = serietvService.findTop3CorrelateByGenere(currentId, genere);
                    
                } catch (Exception e) {
                    System.err.println("Errore nel recupero delle serie TV correlate: " + e.getMessage());
                    // In caso di errore, si mantiene la lista vuota
                }
            }
        }
        
        // Aggiunge la lista al modello per Thymeleaf
        model.addAttribute("serieTvCorrelatePerGenere", serieTvCorrelate);
        // ----------------------- <-- FINE NUOVA SEZIONE
        
        return "scheda-serietv";
    }


    // -----------------------
    // SALVA / AGGIORNA VALUTAZIONE
    // -----------------------
    @PostMapping("/{id}/valuta")
    public String salvaValutazione(
            @PathVariable Long id,
            @RequestParam("voto") Double voto,
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

        valutazioneService.salvaValutazione(idUtente, id, voto, recensione);

        return "redirect:/serietv/" + id;
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

        return "redirect:/serietv/" + id;
    }

    // -----------------------
    // WATCHLIST (placeholder)
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

        // Toggle watchlist
        watchlistService.toggleWatchlist(idUtente, id);

        return "redirect:/serietv/" + id;
    }

}
    
