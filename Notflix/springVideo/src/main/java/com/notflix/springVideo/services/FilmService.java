package com.notflix.springVideo.services;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.FilmConverter;
import com.notflix.springVideo.dto.FilmDTO;
import com.notflix.springVideo.entities.Attore;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.repositories.FilmRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class FilmService extends GenericService<Long, Film, FilmDTO, FilmConverter, FilmRepository> {



    private final FilmRepository filmRepository;

    private final FilmConverter filmConverter;


    //considerare "film" anche gli Intrattenimenti con genere NON  contenente TV
   /* public Page<Film> getAll (Pageable pageable) {
    return filmRepository.findByGenereLike("%TV%", pageable);
    }*/
    public List<Film> getByGenere(String genere) {
        return getRepository().findByGenereIgnoreCase(genere);
    }

     /*public Page<Film> getByGenere(String genere, Pageable pageable) {
        return filmRepository.findByGenereIgnoreCase(genere, pageable);
    }*/
    public Page<Film> getAll(Pageable pageable) {
    return filmRepository.findAllFilms(pageable);
}

    public Page<Film> getByGenere(String genere, Pageable pageable) {
        return filmRepository.findByGenere(genere, pageable);
    }

    
    public Page<Film> cercaPerTitoloOPerAttore (String q, Pageable pageable){
        return filmRepository.cercaPerTitoloOPerAttore(q,pageable);

    }

    public List<Film> getByTitolo(String titolo) {
        return getRepository().findByTitolo(titolo);
    }

    public List<Film> getByRegista(Regista regista) {
        return getRepository().findByRegista(regista);
    }

    // cercare per singolo attore
    public List<Film> getByAttore(Attore attore) {
        return getRepository().findByAttori(Arrays.asList(attore));
    }

    // Cerca per lista di attori
    public List<Film> getByAttori(List<Attore> attori) {
        return getRepository().findByAttori(attori);
    }

    public List<Film> getByAnnoUscita(Date annoUscita) {
        return getRepository().findByAnnoUscita(annoUscita);
    }

    
    public List<Film> findByRangeRating(double minRating, double maxRating) {
        return getRepository().findByRangeRating(minRating, maxRating);
    }

    public Film construct(Map<String, Object> entita) {
        Film film = getContext().getBean(Film.class, entita);
        return film;
    }

    // se ti serve l'entità Film
    public Optional<Film> findByIdSafe(Long id) {
        // Prefer a fetch-join query to eagerly load the cast (attori) to avoid
        // LazyInitializationException when the view accesses the collection.
        try {
            return filmRepository.findByIdWithAttori(id);
        } catch (Exception ex) {
            // fallback to default repository method
            return getRepository().findById(id);
        }
    }

    // quello che serve al controller: DTO sicuro
    public Optional<FilmDTO> findDtoByIdSafe(Long id) {
        try {
            FilmDTO dto = this.getById(id); // metodo ereditato da GenericService
            return Optional.ofNullable(dto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Try to read director name directly from DB joining film->registi->persone.
     * Returns null if not found.
     */
    public String getDirectorNameFromDb(Long filmId) {
        try {
            Object[] r = getRepository().findDirectorNameByFilmId(filmId);
            if (r != null && r.length >= 2) {
                String n = r[0] != null ? r[0].toString() : null;
                String c = r[1] != null ? r[1].toString() : null;
                if ((n != null && !n.isBlank()) || (c != null && !c.isBlank())) {
                    String name = (n != null ? n : "") + " " + (c != null ? c : "");
                    return name.trim();
                }
            }
        } catch (Exception ex) {
            // ignore and return null
        }
        return null;
    }

   // public Page<Film> getAll(Pageable pageable) {
    //    return filmRepository.findAll(pageable);    }
    

   
    public Page<Film> searchByKeyword(String keyword, Pageable pageable) {
    return filmRepository.findByTitoloContainingIgnoreCase(keyword,pageable);
}

    //Metodo per gestire i film correlati
    
    public List<FilmDTO> findTop3CorrelatiByGenere(Long currentId, String genere) {
    if (genere == null || genere.isBlank() || currentId == null) {
        return Collections.emptyList();
    }
    
    // Chiama il metodo definito nel repository
    List<Film> entities = filmRepository.findTop3ByGenereAndIdNot(genere, currentId);
    
    // Mappa le Entity in DTO usando l'istanza iniettata del convertitore
    return entities.stream()
            .map(filmConverter::fromEToD) // Usa l'istanza iniettata
            .collect(Collectors.toList());
}
}
