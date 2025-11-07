package com.notflix.springVideo.services;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.PodcastConverter;
import com.notflix.springVideo.dto.PodcastDTO;
import com.notflix.springVideo.entities.Podcast;
import com.notflix.springVideo.repositories.PodcastRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper=false)
public class PodcastService extends GenericService <Long, Podcast, PodcastDTO, PodcastConverter, PodcastRepository> {
    
     private final PodcastRepository podcastRepository;

     public Page<Podcast> cercaPerTitoloOPresentatore (String q, Pageable pageable){
        return podcastRepository.cercaPerTitoloOPresentatore(q,pageable);

    }

    public Page<Podcast> getAll(Pageable pageable) {
        return podcastRepository.findAll(pageable);
    }

    public Page<Podcast> getByGenere(String genere, Pageable pageable) {
        return podcastRepository.findByGenereIgnoreCase(genere, pageable);
    }

    public List<Podcast> getByPresentatore(String presentatore) {
        return getRepository().findByPresentatore(presentatore);
    }

    public Optional<Podcast> getById(long id) {
        return getRepository().findById(id);
    }
    

    public List<Podcast> getByProgrammazioni(String programmazioni) {
        return getRepository().findByProgrammazioni(programmazioni);
    }

    public Podcast construct(Map<String, Object> mappa) {
        Podcast podcast = getContext().getBean(Podcast.class, mappa);
        return podcast;
    }

}
