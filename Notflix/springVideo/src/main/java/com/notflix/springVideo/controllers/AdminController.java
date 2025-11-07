package com.notflix.springVideo.controllers;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.notflix.springVideo.dto.AttoreDTO;
import com.notflix.springVideo.dto.FilmDTO;
import com.notflix.springVideo.dto.IntrattenimentoDTO;
import com.notflix.springVideo.dto.PodcastDTO;
import com.notflix.springVideo.dto.RegistaDTO;
import com.notflix.springVideo.dto.SerieTvDTO;
import com.notflix.springVideo.dto.UtenteDTO;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.services.AttoreService;
import com.notflix.springVideo.services.FilmService;
import com.notflix.springVideo.services.IntrattenimentoService;
import com.notflix.springVideo.services.PodcastService;
import com.notflix.springVideo.services.RegistaService;
import com.notflix.springVideo.services.SerieTvService;
import com.notflix.springVideo.services.UtenteService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/auth/admin")
@Data
public class AdminController {
    //per gestire tutti comandi che deve getsire un admin 
   // classe session usa il token  e in base al token e al ruolo la visualizzazione delle pagine è ammessa o meno
   //in questo modo non devo scrivere in ogni classe : se sei docente vedi x .

   private final UtenteService utenteService;
   private final AttoreService attoreService;
   private final RegistaService registaService;
   private final FilmService filmService;
   private final SerieTvService serieTvService;
   private final PodcastService podcastService;
   private final IntrattenimentoService intrattenimentoService;


  
   //accesso all'area admin    
    @GetMapping 
   public String areaAdmin(Model model, Principal principal) {
    // Recupera l’utente loggato
    UtenteDTO utente = utenteService.getByUsername(principal.getName());

    if (utente != null) {
        model.addAttribute("utente", utente);
    }

    return "areaAdmin";
}

    //mostra il form di modifica dell'admin
    
    @GetMapping("/modificaProfiloUtente")
    public String mostraProfiloAdmin(Model model, Principal principal) {
        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        if (utenteDTO != null) {
            model.addAttribute("utente", utenteDTO);
        } else {
            model.addAttribute("utente", new UtenteDTO()); // Oggetto vuoto per sicurezza
        }
        return "areaAdmin"; // oppure il template corretto della pagina
    }
    //modifica il profilo dell'admin
     @PostMapping("/modificaProfilo")
    public String updateUtente(@RequestParam Map<String, Object> dati, Model model, Principal principal) {

        UtenteDTO utenteDTO = utenteService.getByUsername(principal.getName());
        Utente utente = utenteService.getRepository().findById(utenteDTO.getId()).orElse(null);
        if (utente == null) {
            model.addAttribute("error", "Utente non trovato");
            return "areaAdmin";
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

        return "areaAdmin";
    }

    //conferma modifica utente 
    @PutMapping("/aggiornaUtenteAdmin")
    @ResponseBody
    public ResponseEntity<String> aggiornaUtenteAdmin(@RequestParam Map<String, String> dati) {
        Long id = Long.valueOf(dati.get("id"));
        Utente utente = utenteService.getRepository().findById(id).orElse(null);

        if (utente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }

        if (dati.containsKey("nome")) utente.setNome(dati.get("nome"));
        if (dati.containsKey("cognome")) utente.setCognome(dati.get("cognome"));
        if (dati.containsKey("username")) utente.setUsername(dati.get("username"));
        if (dati.containsKey("email")) utente.setEmail(dati.get("email"));
        if (dati.containsKey("nazionalita")) utente.setNazionalita(dati.get("nazionalita"));
        if (dati.containsKey("dataNascita") && !dati.get("dataNascita").isBlank()) {
            utente.setDataNascita(LocalDate.parse(dati.get("dataNascita")));
        }
        if (dati.containsKey("ruolo")) utente.setRole(dati.get("ruolo"));

        utenteService.getRepository().save(utente);
        return ResponseEntity.ok("Utente aggiornato");
    }

       // Lista utenti (solo per admin), ricerca
   @GetMapping("/lista-utenti")
    public String listaUtenti(@RequestParam(required = false) String q, Model model) {
    List<Utente> utenti;

    if (q != null && !q.isBlank()) {
        // Ricerca multi-campo
        utenti = utenteService.searchUsers(q);
    } else {
        utenti = utenteService.getAllUtenti();
    }

    model.addAttribute("utenti", utenti);
    return "lista/lista-utenti";
}
 // Lista utenti (solo per admin), tutti
   @GetMapping("/listaUtenti")
    public String mostraListaUtenti(Model model) {
        List<Utente> utenti = utenteService.getRepository().findAll();
        model.addAttribute("utenti", utenti);
        return "admin/lista-utenti";
    }

// Lista film /serie podcast (solo per admin), 
   @GetMapping("/lista-contenuti")
    public String listaContenuti(
         @RequestParam(required = false, name = "tipo", defaultValue = "film") String tipoParam,
        @RequestParam(required = false) String genere,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "20") int size,
        Model model) {

    log.info("➡️ [admin] Tipo: {} | Genere: {} | Page: {}", tipoParam, genere, page);

    Pageable pageable = PageRequest.of(page, size);
    Page<? extends Intrattenimento> contenuti;
    String titoloPagina;

    // Normalizza il parametro tipo (minuscolo e senza spazi)
    String tipoLower = (tipoParam == null) ? "film" : tipoParam.trim().replaceAll("\\s+", "").toLowerCase();

    switch (tipoLower) {
        case "serietv":
            // Usa i metodi del service già esistenti
            contenuti = (genere == null || genere.isBlank())
                    ? serieTvService.getAll(pageable)
                    : serieTvService.getByGenere(genere, pageable);
            titoloPagina = "Catalogo Serie TV";
            break;

        case "podcast":
            contenuti = podcastService.getAll(pageable);
            titoloPagina = "Catalogo Podcast";
            break;

        default:
            contenuti = (genere == null || genere.isBlank())
                    ? filmService.getAll(pageable)
                    : filmService.getByGenere(genere, pageable);
            titoloPagina = "Catalogo Film";
            tipoLower = "film";
            break;
    }

    final String tipo = tipoLower;

    // Imposta il tipo in ciascun contenuto per Thymeleaf
    contenuti.getContent().forEach(c -> c.setTipo(tipo));

    model.addAttribute("titoloPagina", titoloPagina);
    model.addAttribute("tipo", tipo);
    model.addAttribute("genereSelezionato", genere);
    model.addAttribute("contenuti", contenuti.getContent());
    model.addAttribute("currentPage", contenuti.getNumber());
    model.addAttribute("totalPages", contenuti.getTotalPages());

    return "lista/lista-contenuti";
    }


@GetMapping("/lista-contenuti-ricerca")
    public String listaContenutiRicerca( 
               @RequestParam(required = false, name = "q") String query,
        @RequestParam(required = false, name = "tipo", defaultValue = "film") String tipo,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "20") int size,
        Model model) {

    log.info("🔎 [ricerca contenuti] query='{}' | tipo={}", query, tipo);

    Pageable pageable = PageRequest.of(page, size);
    Page<? extends Intrattenimento> risultati;

    if (query != null && !query.isBlank()) {
        // 🔍 Ricerca per parola chiave
        switch (tipo.toLowerCase()) {
            case "serietv":
                risultati = serieTvService.searchByKeyword(query, pageable);
                break;
            /*case "podcast":
                risultati = podcastService.searchByKeyword(query, pageable);
                break;*/
            default:
                risultati = filmService.searchByKeyword(query, pageable);
                tipo = "film";
                break;
        }
    } else {
        // 📜 Nessuna query → mostra tutti
        switch (tipo.toLowerCase()) {
            case "serietv":
                risultati = serieTvService.getAll(pageable);
                break;
            case "podcast":
                risultati = podcastService.getAll(pageable);
                break;
            default:
                risultati = filmService.getAll(pageable);
                tipo = "film";
                break;
        }
    }

    // 🧩 Imposta il tipo nei contenuti
    final String tipoFinal = tipo;
    risultati.getContent().forEach(c -> c.setTipo(tipoFinal));

    // 🔸 Controllo se la lista è vuota
    boolean nessunContenuto = risultati.getContent().isEmpty();

    model.addAttribute("titoloPagina", "Risultati ricerca");
    model.addAttribute("contenuti", risultati.getContent());
    model.addAttribute("tipo", tipo);
    model.addAttribute("genereSelezionato", null);
    model.addAttribute("currentPage", risultati.getNumber());
    model.addAttribute("totalPages", risultati.getTotalPages());
    model.addAttribute("nessunContenuto", nessunContenuto);

    return "lista/lista-contenuti";

}

//apre modale di modifica conenuto 

      @GetMapping("/getContenuto")
@ResponseBody
public ResponseEntity<IntrattenimentoDTO> getContenuto(@RequestParam Long id) {
    // Recupera il contenuto dal repository
    Intrattenimento contenuto = intrattenimentoService.getRepository().findById(id).orElse(null);

    if (contenuto == null) {
        return ResponseEntity.notFound().build();
    }

    // Converti l'entità in DTO
    IntrattenimentoDTO dto = intrattenimentoService.getConverter().fromEToD(contenuto);

    return ResponseEntity.ok(dto);
}

    // Aggiornamento utente 
    @PutMapping("/modificaUtente")
    public String  updateUtente(
       @RequestParam("id") Long id,
                                  @RequestParam(value="nome", required=false) String nome,
                                  @RequestParam(value="cognome", required=false) String cognome,
                                  Model model) {
    if (id == null) {
        model.addAttribute("error", "ID utente mancante!");
        return "lista-utenti";
    }

    Utente utente = utenteService.getRepository().findById(id).orElse(null);
    if (utente == null) {
        model.addAttribute("error", "Utente non trovato!");
        return "lista-utenti";
    }

    if (nome != null) utente.setNome(nome);
    if (cognome != null) utente.setCognome(cognome);

    utenteService.getRepository().save(utente);
    model.addAttribute("success", true);
    model.addAttribute("utente", utente);

    return "lista-utenti";
    }

    // Cancellazione utente
    
    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam Long id, RedirectAttributes redirectAttrs) {
        utenteService.deleteById(id);
        redirectAttrs.addFlashAttribute("success", "Utente eliminato!");
        return "lista-utenti"; // torna alla lista utenti
    }

    // Aggiunta ruolo
    @PatchMapping("/addRole")
    public ResponseEntity<Void> addRole(@RequestParam Map<String,String> dati) {
        utenteService.addRole(dati.get("username"), dati.get("role"));
        return ResponseEntity.ok().build();
    } 

//recupera dati utente
    @GetMapping("/getUser")
@ResponseBody
public ResponseEntity<UtenteDTO> getUser(@RequestParam Long id) {
    Utente utente = utenteService.getRepository().findById(id).orElse(null);
    if (utente == null) {
        return ResponseEntity.notFound().build();
    }
    UtenteDTO dto = utenteService.getConverter().fromEToD(utente);
    return ResponseEntity.ok(dto);
}
    //-------------------------------------------------


     @GetMapping("/uploadFilm")
    public String mostraFormUploadFilm(Model model) {
        model.addAttribute("film", new FilmDTO()); // DTO o entità base
        return "register-film"; // Nome del template Thymeleaf
    }

    // 🔹 Form per caricare una nuova Serie TV
    @GetMapping("/uploadSerieTv")
    public String mostraFormUploadSerieTv(Model model) {
        model.addAttribute("serieTv", new SerieTvDTO()); // DTO o entità base
        return "register-serietv"; // Nome del template Thymeleaf
    }
//modifica serie tv
@PostMapping("/modifica-serieTv")
    public String modificaSerieTv(@RequestParam Long id,
            @RequestParam String regista,  @RequestParam String trama, 
            @RequestParam String episodi, @RequestParam String numero_stagioni, 
            @RequestParam String numero_episodi,
            Model model) {

        SerieTvDTO c = serieTvService.getById(id);
        if (c == null) {
            // classe non esistente: creiamo una nuova
            model.addAttribute("msg", "serieTv non trovata, verrà creata una nuova scheda");
            serieTvService.save(Map.of("regista", regista));
            serieTvService.save(Map.of("trama", trama));
            serieTvService.save(Map.of("episodi", episodi));
            serieTvService.save(Map.of("numero_stagioni", numero_stagioni));
            serieTvService.save(Map.of("numero_episodi", numero_episodi));
            


        } else {
            // classe esistente: aggiorniamo 
            serieTvService.save(Map.of("id", String.valueOf(id),"regista", regista));
            serieTvService.save(Map.of("id", String.valueOf(id),"trama", trama));
            serieTvService.save(Map.of("id", String.valueOf(id),"episodi", episodi));
            serieTvService.save(Map.of("id", String.valueOf(id),"numero_stagioni", numero_stagioni));
            serieTvService.save(Map.of("id", String.valueOf(id),"numero_episodi", numero_episodi));
        }
        // la view che mostra le classi è gestita da Controller
        return "redirect:/lista-serieTv";
    }
//modifica film 
@PostMapping("/modifica-film")
    public String modificaFilm(@RequestParam Long id,
            @RequestParam String regista,  @RequestParam String trama, 
            Model model) {

        FilmDTO c = filmService.getById(id);
        if (c == null) {
            // classe non esistente: creiamo una nuova
            model.addAttribute("msg", "film non trovato, verrà creata una nuova scheda");
            filmService.save(Map.of("regista", regista));
            filmService.save(Map.of("trama", trama));
        } else {
            // classe esistente: aggiorniamo 
            filmService.save(Map.of("id", String.valueOf(id),"regista", regista));
            filmService.save(Map.of("id", String.valueOf(id),"trama", trama));
        }
        // la view che mostra le classi è gestita da Controller
        return "redirect:/lista-film";
    }
//modifica podcast

@PostMapping("/modifica-podcast")
    public String modificaPodcast(@RequestParam Long id,
            @RequestParam String presentatore,  @RequestParam String programmazioni, 
            @RequestParam String numero_episodi,
            Model model) {

        PodcastDTO c = podcastService.getById(id);
        if (c == null) {
            // classe non esistente: creiamo una nuova
            model.addAttribute("msg", "podcast non trovata, verrà creata una nuova scheda");
            podcastService.save(Map.of("presentatore", presentatore));
            podcastService.save(Map.of("programmazioni", programmazioni));
            podcastService.save(Map.of("numero_episodi", numero_episodi));
            


        } else {
            // classe esistente: aggiorniamo 
            serieTvService.save(Map.of("id", String.valueOf(id),"presentatore", presentatore));
            serieTvService.save(Map.of("id", String.valueOf(id),"programmazioni", programmazioni));
            serieTvService.save(Map.of("id", String.valueOf(id),"numero_episodi", numero_episodi));
        }
        // la view che mostra le classi è gestita da Controller
        return "redirect:/lista-podcast";
    }

//modifica attori 
@PostMapping("/modifica-attore")
    public String modificaAttore(@RequestParam Long id,
            @RequestParam String nazionalita,  @RequestParam List<Intrattenimento> filmografia,
            Model model) {

        AttoreDTO c = attoreService.getById(id);
        if (c == null) {
            // classe non esistente: creiamo una nuova
            model.addAttribute("msg", "attore non trovato, verrà creata una nuova scheda");
            attoreService.save(Map.of("nazionalita", nazionalita));
            attoreService.save(Map.of("filmografia", filmografia));
        } else {
            // classe esistente: aggiorniamo la sezione
            attoreService.save(Map.of("id", String.valueOf(id), "nazionalita", nazionalita));
        }
        // la view che mostra le classi è gestita da ControllerClassi sotto /personale/classi
        return "redirect:/lista-attori";


        
    }
//modifica registi
@PostMapping("/modifica-regista")
    public String modificaRegista(@RequestParam Long id,
            @RequestParam String nazionalita,  @RequestParam List<Intrattenimento> filmografia,
            Model model) {

        RegistaDTO c = registaService.getById(id);
        if (c == null) {
            // classe non esistente: creiamo una nuova
            model.addAttribute("msg", "regista non trovato, verrà creata una nuova scheda");
            registaService.save(Map.of("nazionalita", nazionalita));
            registaService.save(Map.of("filmografia", filmografia));
        } else {
            // classe esistente: aggiorniamo la sezione
            registaService.save(Map.of("id", String.valueOf(id), "nazionalita", nazionalita));
        }
        // la view che mostra le classi è gestita da ControllerClassi sotto /personale/classi
        return "redirect:/lista-registi";
    }


     //----------------------------------------------------


    

 // Recupera tutti gli utenti
    @GetMapping("/all")
    public ResponseEntity<List<UtenteDTO>> getAllUtenti() {
        List<UtenteDTO> utenti = utenteService.getAll();
        return ResponseEntity.ok(utenti);
    }

    // Recupera utente per ID 
    @GetMapping("/byId")
    public ResponseEntity<UtenteDTO> getUtenteById(@RequestParam Long id) {
        UtenteDTO dto = utenteService.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // Recupera utente per username 
    @GetMapping("/byUsername")
    public ResponseEntity<UtenteDTO> getByUsername(@RequestParam String username) {
        UtenteDTO dto = utenteService.getByUsername(username);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    // Recupera utente per email
    @GetMapping("/byEmail")
    public ResponseEntity<UtenteDTO> getByEmail(@RequestParam String email) {
        UtenteDTO dto = utenteService.getByEmail(email);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }


}
