package com.notflix.springVideo.controller_rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.notflix.springVideo.services.PodcastService;
import com.notflix.springVideo.dto.PodcastDTO;
import com.notflix.springVideo.entities.Podcast;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/podcasts")
public class PodcastControllerRest {

    private final PodcastService podcastService;

    @Autowired
    public PodcastControllerRest(PodcastService podcastService) {
        this.podcastService = podcastService;
    }

    @GetMapping
    public ResponseEntity<List<Podcast>> getAll() {
        return ResponseEntity.ok(podcastService.getRepository().findAll());
    }

    @GetMapping("byId/{id}")
    public ResponseEntity<Podcast> getById(@PathVariable Long id) {
        Optional<Podcast> podcast = podcastService.getRepository().findById(id);
        return podcast.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Podcast> create(@RequestBody Podcast podcast) {
        Podcast s = podcastService.getRepository().save(podcast);
        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Podcast> update(@PathVariable Long id, @RequestBody Podcast podcast) {
        if (!podcastService.getRepository().existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        podcast.setId(id);
        Podcast updatedPodcast = podcastService.getRepository().save(podcast);
        return ResponseEntity.ok(updatedPodcast);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!podcastService.getRepository().existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        podcastService.getRepository().deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Podcast>> search(
            @RequestParam(required = false) String presentatore,
            @RequestParam(required = false) String programmazioni,
            @RequestParam(required = false) int numero_episodi) 
            {

        if (presentatore != null) {
            return ResponseEntity.ok(podcastService.getByPresentatore(presentatore));
        }
        if (programmazioni != null) {
            String d = programmazioni;
            return ResponseEntity.ok(podcastService.getByProgrammazioni(d));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/search/by-id") //cerco per id
    public ResponseEntity<PodcastDTO> searchByRegista(@RequestBody Podcast podcast) {
        return ResponseEntity.ok(podcastService.getById(podcast.getId()));
    }

} 
    
    