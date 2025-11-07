package com.notflix.springVideo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Podcast;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.repositories.FilmRepository;
import com.notflix.springVideo.repositories.PodcastRepository;
import com.notflix.springVideo.repositories.SerieTvRepository;
import com.notflix.springVideo.services.FilmService;
import com.notflix.springVideo.services.PodcastService;
import com.notflix.springVideo.services.SerieTvService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ricerca")
@RequiredArgsConstructor
public class SearchController {
    
   // private final IntrattenimentoRepository intrattenimentoRepository;
    //private final IntrattenimentoService intrattenimentoService;
    //private final AttoreService attoreService;
    //private final RegistaService registaService;
    private final FilmService filmService;
    private final SerieTvService serieTvService;
    private final PodcastService podcastService;
    
    private final FilmRepository filmRepository;
    private final SerieTvRepository serieTVRepository;
    private final PodcastRepository podcastRepository;
    
    @GetMapping("/risultati")
    public String ricercaGlobale(
        
     @RequestParam String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        Model model) {

    Pageable pageable = PageRequest.of(page, size);

    Page<Film> filmResults = filmRepository.cercaPerTitoloOPerAttore(q, pageable);
    Page<SerieTV> serieResults = serieTVRepository.cercaPerTitoloOPerAttore(q, pageable);
    Page<Podcast> podcastResults = podcastRepository.cercaPerTitoloOPresentatore(q, pageable);

    // 🔹 Combina i risultati in un'unica lista (come il template si aspetta)
    List<Object> tuttiRisultati = new ArrayList<>();
    tuttiRisultati.addAll(filmResults.getContent());
    tuttiRisultati.addAll(serieResults.getContent());
    tuttiRisultati.addAll(podcastResults.getContent());

    // 🔹 Calcola i dati per la paginazione (usando il numero maggiore di pagine)
    int totalPages = Math.max(
        Math.max(filmResults.getTotalPages(), serieResults.getTotalPages()),
        podcastResults.getTotalPages()
    );

    model.addAttribute("contenuti", tuttiRisultati);
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("titoloPagina", "Risultati per: " + q);
    model.addAttribute("tipo", "tutti");
    model.addAttribute("genereSelezionato", null);
    model.addAttribute("query", q);

        return "lista/risultati-ricerca";
    }
    
//apre la scheda del film/serie/podcast
     @GetMapping("/scheda/{tipo}/{id}")
    public String schedaContenuto(@PathVariable String tipo, @PathVariable Long id, Model model) {
        switch(tipo) {
            case "film":
                model.addAttribute("item", filmService.findDtoByIdSafe(id));
                return "scheda/scheda-film";
                
            case "serietv":
                model.addAttribute("item", serieTvService.findDtoByIdSafe(id));
                return "scheda/scheda-contenuto";
                
            case "podcast":
                model.addAttribute("item", podcastService.getById(id));
            return "scheda/scheda-contenuto";
        }
        return "error";
        
    }

//mostra il catalogo  con filtri
    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(required = false, name = "tipo", defaultValue = "film") String tipoParam,
        @RequestParam(required = false) String genere,
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "20") int size,
        Model model) {

    log.info("➡️ [catalogo] Tipo: {} | Genere: {} | Page: {}", tipoParam, genere, page);

    Pageable pageable = PageRequest.of(page, size);
    Page<? extends Intrattenimento> contenuti;
    String titoloPagina;

    String tipoLower = (tipoParam == null) ? "film" : tipoParam.toLowerCase();

    switch (tipoLower) {
        case "serietv":
            contenuti = (genere == null || genere.isBlank())
                    ? serieTvService.getAll(pageable)
                    : serieTvService.getByGenere(genere, pageable);
            titoloPagina = "Catalogo Serie TV";
            tipoLower = "serietv";
            break;

        case "podcast":
            contenuti = podcastService.getAll(pageable);
            titoloPagina = "Catalogo Podcast";
            tipoLower = "podcast";
            break;

        default:
            contenuti = (genere == null || genere.isBlank())
                    ? filmService.getAll(pageable)
                    : filmService.getByGenere(genere, pageable);
            titoloPagina = "Catalogo Film";
            tipoLower = "film";
            break;
    }

    // Usa una variabile finale per il lambda (deve essere effectively final)
    final String tipo = tipoLower;

    // Imposta il tipo in ciascun contenuto per usare in Thymeleaf
    contenuti.getContent().forEach(c -> c.setTipo(tipo));

    model.addAttribute("titoloPagina", titoloPagina);
    model.addAttribute("tipo", tipo);
    model.addAttribute("genereSelezionato", genere);
    model.addAttribute("contenuti", contenuti.getContent());
    model.addAttribute("currentPage", contenuti.getNumber());
    model.addAttribute("totalPages", contenuti.getTotalPages());

        return "lista/risultati-ricerca";
    }



    /*// Lista film
    @GetMapping("/catalogo/lista-film")
    public String listaFilm(Model model) {
        try {
            System.out.println("Richiesta lista film ricevuta");
            var films = filmService.getAll(); // Usa il metodo getAll() che restituisce DTO
            System.out.println("Film recuperati: " + (films != null ? films.size() : "null"));
            if (films != null) {
                films.forEach(film -> {
                    System.out.println("Film trovato: " + film.getTitolo());
                });
            }
            model.addAttribute("titoloPagina", "Catalogo Film");
            model.addAttribute("tipo", "film");
            model.addAttribute("contenuti", films);
            System.out.println("Model attributes set, returning view");
            return "lista/risultati-ricerca";
        } catch (Exception e) {
            System.err.println("Errore durante il recupero dei film: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // Lista serie TV
   @GetMapping("/catalogo/lista-serieTv")
public String listaSerie(Model model) {
    try{
        System.out.println("Richiesta lista serieTV ricevuta");
        var series = serieTvService.getAll();
        System.out.println("serie tv recuperate: " + (series != null ? series.size() : "null"));
        if (series != null) {
            series.forEach(serie -> {
                System.out.println("serie trovate: " + serie.getTitolo());
            });
        }
        model.addAttribute("titoloPagina", "Catalogo Serie TV");
        model.addAttribute("tipo", "serie");
        model.addAttribute("contenuti", series);
        System.out.println("Model attributes set, returning view");
        return "lista/risultati-ricerca";

    } catch (Exception e) {
            System.err.println("Errore durante il recupero delle serie: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "error";
        }
   
   
}
    // Lista podcast
    @GetMapping("/catalogo/lista-podcast")
    public String listaPodcast(Model model) {
        model.addAttribute("titoloPagina", "Catalogo Podcast");
        model.addAttribute("tipo", "podcast");
        model.addAttribute("contenuti", podcastService.getAll());
        return "lista/risultati-ricerca";
    }

    
    /*gestione filtri
    @GetMapping("/film")
    public String listaFilm(@RequestParam(required = false) String q,
                        @RequestParam(required = false) String genere,
                        Model model) {
    List<Film> risultati = filmService.filtra(q, genere);
    model.addAttribute("contenuti", risultati);
    model.addAttribute("tipo", "film");
    model.addAttribute("titoloPagina", "Catalogo Film");
    return "listaContenuti";
}*/



}