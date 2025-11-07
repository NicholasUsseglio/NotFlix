package com.notflix.springVideo.converters;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.FilmDTO;
import com.notflix.springVideo.entities.Film;

@Service
public class FilmConverter implements GenericConverter<Film, FilmDTO> {

    @Override
    public Film fromDToE(FilmDTO dto) {
        Film film = new Film();
        film.setId(dto.getId());
        film.setTitolo(dto.getTitolo());
        film.setGenere(dto.getGenere());
        film.setDurata(dto.getDurata());
        film.setAnnoUscita(dto.getAnnoUscita());
        film.setRating(dto.getRating());
        film.setTrama(dto.getTrama());
        film.setRegista(dto.getRegista());
        return film;
    }

    @Override
    public FilmDTO fromEToD(Film e) {
        FilmDTO dto = new FilmDTO();
        dto.setId(e.getId());
        dto.setTitolo(e.getTitolo());
        dto.setGenere(e.getGenere());
        dto.setDurata(e.getDurata());
        dto.setAnnoUscita(e.getAnnoUscita());
        dto.setRating(e.getRating());
        dto.setTrama(e.getTrama());
        dto.setRegista(e.getRegista());        
        dto.setAttori(e.getAttori());          
        return dto;
    }

}
