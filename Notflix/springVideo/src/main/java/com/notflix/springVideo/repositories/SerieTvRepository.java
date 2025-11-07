package com.notflix.springVideo.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.notflix.springVideo.entities.Attore;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Regista;

@Repository
public interface SerieTvRepository extends JpaRepository<SerieTV, Long> {

   /*@Query("SELECT s FROM SerieTV s WHERE LOWER(s.genere)  LIKE LOWER(:pattern)")
    Page<SerieTV> findByGenereLike(@Param("pattern") String pattern, Pageable pageable);

    
    @Query("SELECT s FROM SerieTV s WHERE (:genere IS NULL OR LOWER(s.genere) LIKE LOWER(CONCAT('%', :genere, '%')))")
    Page<SerieTV> findByGenere(@Param("genere") String genere, Pageable pageable);
    
    //Page<SerieTV> findByGenereIgnoreCase(String genere, Pageable pageable);

*/


    @Query("SELECT s FROM SerieTV s WHERE LOWER(s.genere) LIKE '%tv%'")
    Page<SerieTV> getAll(Pageable pageable);

    /*@Query("SELECT s FROM SerieTV s WHERE LOWER(s.genere) LIKE '%tv%' AND (:genere IS NULL OR LOWER(s.genere) LIKE LOWER(CONCAT('%', :genere, '%')))")
    Page<SerieTV> getByGenere(@Param("genere") String genere, Pageable pageable);*/

    @Query("SELECT s FROM SerieTV s WHERE (:genere IS NULL OR LOWER(s.genere) LIKE LOWER(CONCAT('%', :genere, '%'))) AND LOWER(s.genere) LIKE '%tv%'")
Page<SerieTV> getByGenere(@Param("genere") String genere, Pageable pageable);

@Query("SELECT s FROM SerieTV s WHERE LOWER(s.genere) LIKE '%tv%'")
Page<SerieTV> getAllSeries(Pageable pageable);


    @Query("""
    SELECT DISTINCT s FROM SerieTV s
    LEFT JOIN s.attori a
    WHERE LOWER(s.titolo) LIKE LOWER(CONCAT('%', :q, '%'))
    OR LOWER(s.genere) LIKE LOWER(CONCAT('%', :q, '%'))
    OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :q, '%'))
    OR LOWER(a.cognome) LIKE LOWER(CONCAT('%', :q, '%'))
    """)
   
    Page<SerieTV> cercaPerTitoloOPerAttore(@Param("q") String query, Pageable pageable);

   

    Page<SerieTV> findAll(Pageable pageable);

    List<SerieTV> findAll();

    List<SerieTV> findByTitolo(String titolo);

    List<SerieTV> findByRegista(Regista regista);

    List<SerieTV> findByAttori(List<Attore> attori);

    List<SerieTV> findByAnnoUscita(Date annoUscita);

    List<SerieTV> findByGenereIgnoreCase(String genere);

    @Query("SELECT s FROM SerieTV s WHERE s.rating BETWEEN :minRating AND :maxRating")
    List<SerieTV> findByRangeRating(@Param("minRating") double minRating, @Param("maxRating") double maxRating);

    // Query nativa per ottenere nome e cognome del regista associato alla serie TV
    @Query(value = "SELECT p.nome, p.cognome FROM serietv s " +
                   "JOIN registi r ON s.id_regista = r.id " +
                   "JOIN persone p ON r.id_persona = p.id " +
                   "WHERE s.id = :serietvId", nativeQuery = true)
    Object[] findDirectorNameBySerietvId(@Param("serietvId") Long serietvId);

    // Evita problemi con lazy loading del cast
    @Query("SELECT s FROM SerieTV s LEFT JOIN FETCH s.attori WHERE s.id = :id")
    java.util.Optional<SerieTV> findByIdWithAttori(@Param("id") Long id);

    /* lo commento perché crea problemi al filtro serie tv in catalogo!!!!!
    // Recupera tutte le serie TV (solo quelle con tipo='serietv')
    @Query("SELECT s FROM SerieTV s WHERE LOWER(s.tipo) = 'serietv'")
    Page<SerieTV> findAllSerieTv(Pageable pageable);

    */
      Page<SerieTV> findByTitoloContainingIgnoreCase(String titolo, Pageable pageable);

    List<SerieTV> findTop3ByGenereAndIdNot(String genere, Long currentId);

}
