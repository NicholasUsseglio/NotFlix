package com.notflix.springVideo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notflix.springVideo.entities.Intrattenimento;

@Repository
public interface IntrattenimentoRepository extends JpaRepository<Intrattenimento, Long> {

        Page<Intrattenimento> findAll(Pageable pageable);

       @Query("""
    SELECT DISTINCT i
    FROM Intrattenimento i
    LEFT JOIN TREAT(i AS Film).attori fa
    LEFT JOIN TREAT(i AS SerieTV).attori sa
    WHERE LOWER(COALESCE(i.titolo, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(i.genere, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(fa.nome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(fa.cognome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(sa.nome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR LOWER(COALESCE(sa.cognome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
       OR (TYPE(i) = Podcast AND LOWER(COALESCE(TREAT(i AS Podcast).presentatore, '')) LIKE LOWER(CONCAT('%', :q, '%')))
           """)
        Page<Intrattenimento> cercaGlobale(@Param("q") String query, Pageable pageable);


}

