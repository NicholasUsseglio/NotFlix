package com.notflix.springVideo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.notflix.springVideo.dto.AttoreDTO;
import com.notflix.springVideo.entities.Attore;
import com.notflix.springVideo.services.AttoreService;

import jakarta.servlet.http.HttpSession;
import lombok.Data;

@Controller
@Data
public class AttoreController {

    private final AttoreService attoreService;

    @GetMapping("/attori")
    public String readAll(Model model){
        List<AttoreDTO> lista = attoreService.getAll();
        model.addAttribute("lista", lista);
        return "attori.html";
    }
    
    // metodo che risponde alla request di ricerca di uno attore per nominativo
    @GetMapping("/cerca-attore")
    public String getAttoreByName(@RequestParam(required = false) String nome,
                                  @RequestParam(required = false) String cognome,
                                  Model model) {
        List<Attore> lista = attoreService.getByNameAndSurname(nome, cognome);
        model.addAttribute("lista", lista);
        return "attori.html";
    }

   /* @GetMapping("/api/studenti")
    @ResponseBody
    public List<Studente> cercaStudenti(@RequestParam(required = false) String nome,
                                        @RequestParam(required = false) String cognome) {
        return serviceStudenti.getByNameAndSurname(nome, cognome);
    }*/

    //per admin: 

    // http://localhost:8080/elimina-studente?idStudente={valore}
    @GetMapping("/elimina-attore")
    public String elimina(@RequestParam(defaultValue = "0L", required = true) Long idAttore, HttpSession session) {
        if(((String)session.getAttribute("role")).equalsIgnoreCase("ADMIN")){ //passo la session per la sicurezza//casto a stringa perché session ritorna degli object
            if (idAttore > 0) {
                attoreService.delete(idAttore);
            }
        }
        return "redirect:/attori";
    }

    @PostMapping("/inserisci-attore")
    public String inserisci(@RequestParam Map<String,Object> map){
        attoreService.save(map);
        return "redirect:/attori";
    }

    @PostMapping("/modifica-attore")
    public String aggiorna(@RequestParam Map<String,Object> map){
        attoreService.save(map);
        return "redirect:/attori";
    }

    /* @GetMapping("/users/list")
    public String usersList(Model model) {
        model.addAttribute("users", serviceStudenti.getAll());
        return "fragments/userList :: userList";
    }*/

    // -----------------------
    // NUOVO METODO: scheda singolo attore (dettaglio)
    // -----------------------
    @GetMapping("/attore/{id}")
    public String getSchedaAttore(@PathVariable Long id, Model model) {
        try {
            // Usa un metodo safe che non esplode se non trova
            var maybeAttore = attoreService.findByIdSafe(id); // se non esiste, ritorna Optional.empty()
            if (maybeAttore.isEmpty()) {
                return "attore-non-trovato"; // template di fallback
            }

            var attore = maybeAttore.get();

            // ✅ FIX: assegna il tipo corretto ("film" o "serietv") ad ogni elemento della filmografia
            if (attore.getFilmografia() != null) {
                attore.getFilmografia().forEach(f -> {
                    if (f.getTipo() == null || f.getTipo().isBlank()) {
                        if (f.getGenere() != null && f.getGenere().toUpperCase().contains("TV")) {
                            f.setTipo("serietv");
                        } else {
                            f.setTipo("film");
                        }
                    }

                    if (f.getTipo() != null) {
                        f.setTipo(f.getTipo().toLowerCase());
                    }
                    
                    // Log di debug (facoltativo)
                    System.out.println("[DEBUG] " + f.getTitolo() + " → tipo: " + f.getTipo());
                });
            }

            model.addAttribute("attore", attore);
            model.addAttribute("filmografia", attore.getFilmografia());

            return "scheda-attore";

        } catch (Exception e) {
            System.err.println("[ERRORE] Caricamento attore id " + id + ": " + e.getMessage());
            return "attore-non-trovato";
        }
    }
}
