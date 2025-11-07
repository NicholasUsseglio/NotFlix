package com.notflix.springVideo.converters;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.SerieTvDTO;
import com.notflix.springVideo.entities.SerieTV;

@Service
public class SerieTvConverter implements GenericConverter<SerieTV, SerieTvDTO> {

    @Override
    public SerieTV fromDToE(SerieTvDTO dto) {
        SerieTV serie = new SerieTV();
        serie.setId(dto.getId());
        serie.setTitolo(dto.getTitolo());
        serie.setGenere(dto.getGenere());
        serie.setTrama(dto.getTrama());
        serie.setEpisodi(dto.getEpisodi());
        serie.setNumeroStagioni(dto.getNumeroStagioni());
        serie.setNumeroEpisodi(dto.getNumeroEpisodi());
        serie.setDurata(dto.getDurata());
        serie.setAnnoUscita(dto.getAnnoUscita());
        serie.setRegista(dto.getRegista());
        return serie;
    }

    @Override
    public SerieTvDTO fromEToD(SerieTV e) {
        SerieTvDTO dto = new SerieTvDTO();
        dto.setId(e.getId());
        dto.setTitolo(e.getTitolo());
        dto.setGenere(e.getGenere());
        dto.setTrama(e.getTrama());
        dto.setEpisodi(e.getEpisodi());
        dto.setNumeroStagioni(e.getNumeroStagioni());
        dto.setNumeroEpisodi(e.getNumeroEpisodi());
        dto.setDurata(e.getDurata());
        dto.setAnnoUscita(e.getAnnoUscita());
        dto.setRegista(e.getRegista());
        dto.setTipo(e.getTipo() != null ? e.getTipo() : "serietv");
        return dto;
    }
}
