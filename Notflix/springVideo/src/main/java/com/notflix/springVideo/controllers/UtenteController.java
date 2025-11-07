package com.notflix.springVideo.controllers;

import lombok.Data;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.notflix.springVideo.converters.ValutazioneConverter;
import com.notflix.springVideo.dto.PreferitoDTO;
import com.notflix.springVideo.dto.UtenteDTO;
import com.notflix.springVideo.dto.ValutazioneDTO;
import com.notflix.springVideo.dto.WatchListDTO;
import com.notflix.springVideo.entities.Preferito;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.entities.Valutazione;
import com.notflix.springVideo.entities.Watchlist;
import com.notflix.springVideo.repositories.PreferitoRepository;
import com.notflix.springVideo.services.PreferitoService;
import com.notflix.springVideo.services.UtenteService;
import com.notflix.springVideo.services.WatchlistService;

@Controller
@RequestMapping("/areaUtente")
@Data
public class UtenteController {

    private final UtenteService utenteService;
    private final PreferitoService preferitoService;
    private final PreferitoRepository preferitoRepository;
    private final WatchlistService watchlistService;
    @Autowired
    private ValutazioneConverter valutazioneConverter;

    // --- Mostra l'area utente ---
   @GetMapping
    public String mostraAreaUtente(Model model, Principal principal) {
        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        model.addAttribute("utente", utenteDTO);

        // --- Preferiti ---
List<Preferito> tuttiPreferiti = preferitoRepository.findByUtente_Id(utenteDTO.getId());

List<PreferitoDTO> filmPreferitiDTO = tuttiPreferiti.stream()
        .filter(p -> p.getIntrattenimento() != null &&
                     "film".equalsIgnoreCase(p.getIntrattenimento().getTipo()))
        .map(utenteService.getPreferitoConverter()::fromEToD)
        .toList();

List<PreferitoDTO> seriePreferiteDTO = tuttiPreferiti.stream()
        .filter(p -> p.getIntrattenimento() != null &&
                     ("serietv".equalsIgnoreCase(p.getIntrattenimento().getTipo())
                      || "serie".equalsIgnoreCase(p.getIntrattenimento().getTipo())))
        .map(utenteService.getPreferitoConverter()::fromEToD)
        .toList();

model.addAttribute("filmPreferiti", filmPreferitiDTO);
model.addAttribute("seriePreferite", seriePreferiteDTO);

        // --- To Watch ---
List<Watchlist> toWatchList = watchlistService.findByUtente(utenteDTO.getId());

// Converti con il converter
List<WatchListDTO> filmToWatchDTO = toWatchList.stream()
        .filter(w -> w.getIntrattenimento() != null &&
                     "film".equalsIgnoreCase(w.getIntrattenimento().getTipo()))
        .map(utenteService.getWatchlistConverter()::fromEToD)
        .toList();

List<WatchListDTO> serieToWatchDTO = toWatchList.stream()
        .filter(w -> w.getIntrattenimento() != null &&
                     ("serietv".equalsIgnoreCase(w.getIntrattenimento().getTipo())
                      || "serie".equalsIgnoreCase(w.getIntrattenimento().getTipo())))
        .map(utenteService.getWatchlistConverter()::fromEToD)
        .toList();

model.addAttribute("filmToWatch", filmToWatchDTO);
model.addAttribute("serieToWatch", serieToWatchDTO);
List<Valutazione> recensioni = utenteService.getRecensioniUtente(utenteDTO.getId());

List<ValutazioneDTO> recensioniDTO = recensioni.stream()
        .map(valutazioneConverter::fromEToD)
        .toList();

model.addAttribute("recensioniUtente", recensioniDTO);

        return "areaUtente";
    }

    // --- Toggle Preferito ---
    @PostMapping("/aggiornaPreferito")
    @ResponseBody
    public ResponseEntity<String> aggiornaPreferito(
            @RequestParam Long idIntrattenimento,
            Principal principal) {

        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        boolean aggiunto = preferitoService.togglePreferito(utenteDTO.getId(), idIntrattenimento);

        if (aggiunto) {
            return ResponseEntity.ok("Aggiunto ai preferiti!");
        } else {
            return ResponseEntity.ok("Rimosso dai preferiti!");
        }
    }

    // --- Toggle To Watch ---
    @PostMapping("/aggiornaToWatch")
    @ResponseBody
    public ResponseEntity<String> aggiornaToWatch(
            @RequestParam Long idIntrattenimento,
            Principal principal) {

        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        boolean aggiunto = watchlistService.toggleWatchlist(utenteDTO.getId(), idIntrattenimento);

        if (aggiunto) {
            return ResponseEntity.ok("Aggiunto alla Watchlist!");
        } else {
            return ResponseEntity.ok("Rimosso dalla Watchlist!");
        }
    }

    // --- Rimuovi To Watch (opzionale, bottone ❌) ---
    @DeleteMapping("/rimuoviToWatch")
    @ResponseBody
    public ResponseEntity<Void> rimuoviDaToWatch(
            @RequestParam Long idIntrattenimento,
            Principal principal) {

        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        watchlistService.toggleWatchlist(utenteDTO.getId(), idIntrattenimento); // toggle per rimuovere

        return ResponseEntity.ok().build();
    }

    // --- Funzioni CRUD utente ---
    @PostMapping("/insert")
    @ResponseBody
    public ResponseEntity<Boolean> insertUtente(@RequestParam Map<String, Object> dati) {
        boolean result = utenteService.save(dati);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    public String updateUtente(@RequestParam Map<String, Object> dati, Model model, Principal principal) {

        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        Utente utente = utenteService.getRepository().findById(utenteDTO.getId()).orElse(null);
        if (utente == null) {
            model.addAttribute("error", "Utente non trovato");
            return "areaUtente";
        }

        if(dati.containsKey("nome")) utente.setNome((String)dati.get("nome"));
        if(dati.containsKey("cognome")) utente.setCognome((String)dati.get("cognome"));
        if(dati.containsKey("username")) utente.setUsername((String)dati.get("username"));
        if(dati.containsKey("email")) utente.setEmail((String)dati.get("email"));
        if(dati.containsKey("nazionalita")) utente.setNazionalita((String)dati.get("nazionalita"));
        if(dati.containsKey("dataNascita") && dati.get("dataNascita") != null) {
            utente.setDataNascita(LocalDate.parse((String)dati.get("dataNascita")));
        }

        utenteService.getRepository().save(utente);
        utenteDTO = utenteService.getConverter().fromEToD(utente);
        model.addAttribute("utente", utenteDTO);
        model.addAttribute("success", true);

        return "areaUtente";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteUtente(@RequestParam Long id) {
        utenteService.delete(id);
        return ResponseEntity.ok().build();
    }
}

