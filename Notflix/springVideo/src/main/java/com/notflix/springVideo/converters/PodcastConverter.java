package com.notflix.springVideo.converters;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.PodcastDTO;
import com.notflix.springVideo.entities.Podcast;

@Service
public class PodcastConverter implements GenericConverter<Podcast, PodcastDTO> {

    @Override
    public Podcast fromDToE(PodcastDTO dto) {
        Podcast p = new Podcast();
        p.setId(dto.getId());
        p.setPresentatore(dto.getPresentatore());
        p.setNumero_episodi(dto.getNumero_episodi());
        p.setProgrammazioni(dto.getProgrammazioni());
        return p;
    }

    @Override
    public PodcastDTO fromEToD(Podcast e) {
        PodcastDTO dto = new PodcastDTO();
        dto.setId(dto.getId());
        dto.setPresentatore(dto.getPresentatore());
        dto.setNumero_episodi(dto.getNumero_episodi());
        dto.setProgrammazioni(dto.getProgrammazioni());
        return dto;
    }
    
}
