package com.notflix.springVideo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notflix.springVideo.entities.Podcast;






@Repository
public interface PodcastRepository extends JpaRepository <Podcast, Long>{

   /*@Query("""
    SELECT p FROM Podcast p
    WHERE LOWER(p.titolo) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(p.presentatore, '')) LIKE LOWER(CONCAT('%', :q, '%'))
    """)*/

    @Query("""
    SELECT DISTINCT p FROM Podcast p
    WHERE LOWER(p.titolo) LIKE LOWER(CONCAT('%', :q, '%'))
    OR LOWER(p.presentatore) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
Page<Podcast> cercaPerTitoloOPresentatore(@Param("q") String q, Pageable pageable);

     Page<Podcast> findByGenereIgnoreCase(String genere, Pageable pageable);
    Page<Podcast> findAll(Pageable pageable);

    List<Podcast> findByPresentatore(String nome);

    List<Podcast> findById(Podcast podcast);


    List<Podcast> findByProgrammazioni(String programmazioni);


}
