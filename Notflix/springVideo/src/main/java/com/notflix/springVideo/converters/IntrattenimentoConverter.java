package com.notflix.springVideo.converters;


import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.IntrattenimentoDTO;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.SerieTV;

@Service
public class IntrattenimentoConverter implements GenericConverter<Intrattenimento, IntrattenimentoDTO> {

    
    @Override
    public Intrattenimento fromDToE(IntrattenimentoDTO dto) {
        Intrattenimento contenuto;
        switch(dto.getTipo()) {
            case "film":
                contenuto = new Film();
                break;
            case "serietv":
                contenuto = new SerieTV();
                break;
            default:
                throw new IllegalArgumentException("Tipo non supportato: " + dto.getTipo());
        }

        contenuto.setId(dto.getId());
        contenuto.setTitolo(dto.getTitolo());
        contenuto.setGenere(dto.getGenere());
        contenuto.setDurata(dto.getDurata());
        contenuto.setAnnoUscita(dto.getAnnoUscita());
        contenuto.setRating(dto.getRating());

        return contenuto;
    }

    @Override
    public IntrattenimentoDTO fromEToD(Intrattenimento e) {
        IntrattenimentoDTO dto = new IntrattenimentoDTO();
        dto.setId(e.getId());
        dto.setTitolo(e.getTitolo());
        dto.setGenere(e.getGenere());
        dto.setDurata(e.getDurata());
        dto.setAnnoUscita(e.getAnnoUscita());
        dto.setRating(e.getRating());
        dto.setTipo(e instanceof Film ? "film" : "serietv"); // ecc.

        return dto;
    }
}