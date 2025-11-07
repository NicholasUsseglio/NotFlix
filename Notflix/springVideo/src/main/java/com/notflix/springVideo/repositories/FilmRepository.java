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
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Regista;



@Repository
public interface FilmRepository extends JpaRepository <Film, Long>{

    

    /*@Query(""" 
             SELECT DISTINCT f FROM Film f
            LEFT JOIN f.attori a
            WHERE LOWER(COALESCE(f.titolo, '')) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(COALESCE(f.genere, '')) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(COALESCE(a.nome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(COALESCE(a.cognome, '')) LIKE LOWER(CONCAT('%', :q, '%'))
            """)*/
           
   @Query("""
            SELECT DISTINCT f FROM Film f
            LEFT JOIN f.attori a
            WHERE LOWER(f.titolo) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(f.genere) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :q, '%'))
            OR LOWER(a.cognome) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<Film> cercaPerTitoloOPerAttore(@Param("q") String query, Pageable pageable);
    

    @Query("SELECT f FROM Film f WHERE LOWER(f.genere) NOT LIKE LOWER(:pattern)")
    Page<Film> findByGenereLike(@Param("pattern") String pattern, Pageable pageable);

    /*@Query("SELECT f FROM Film f WHERE (:genere IS NULL OR LOWER(f.genere) LIKE LOWER(CONCAT('%', :genere, '%')))")
    Page<Film> findByGenere(@Param("genere") String genere, Pageable pageable);*/


    @Query("SELECT f FROM Film f WHERE (:genere IS NULL OR LOWER(f.genere) LIKE LOWER(CONCAT('%', :genere, '%'))) AND LOWER(f.genere) NOT LIKE '%tv%'")
Page<Film> findByGenere(@Param("genere") String genere, Pageable pageable);

@Query("SELECT f FROM Film f WHERE LOWER(f.genere) NOT LIKE '%tv%'")
Page<Film> findAllFilms(Pageable pageable);
    /*@Query("SELECT f FROM Film f WHERE (:genere IS NULL OR LOWER(f.genere) LIKE LOWER(CONCAT('%', :genere, '%'))) AND LOWER(f.genere) NOT LIKE '%tv%'")
    Page<Film> findByGenere(@Param("genere") String genere, Pageable pageable);
*/
//    Page<Film> findByGenereIgnoreCase(String genere, Pageable pageable);

    Page<Film> findAll(Pageable pageable);


    List<Film> findAll();
    
    List<Film> findByTitolo(String titolo);

    List<Film> findByRegista(Regista regista);

    List<Film> findByAttori(List<Attore> attori);

    List<Film> findByAnnoUscita(Date annoUscita);

    List<Film> findByGenereIgnoreCase(String genere);

    @Query("SELECT s FROM Film s WHERE s.rating BETWEEN :minRating AND :maxRating")
    List<Film> findByRangeRating(@Param("minRating") double minRating, @Param("maxRating") double maxRating);

    // fetch film together with its cast (attori) to avoid LazyInitializationException
    @Query("SELECT f FROM Film f LEFT JOIN FETCH f.attori WHERE f.id = :id")
    java.util.Optional<Film> findByIdWithAttori(@Param("id") Long id);

    // native query fallback: get director's nome and cognome joining film->registi->persone
    @Query(value = "SELECT p.nome, p.cognome FROM film f JOIN registi r ON f.id_regista = r.id JOIN persone p ON r.id_persona = p.id WHERE f.id = :filmId", nativeQuery = true)
    Object[] findDirectorNameByFilmId(@Param("filmId") Long filmId);

      Page<Film> findByTitoloContainingIgnoreCase(String titolo, Pageable pageable);

      // Metodo per gestire i film correlati
    List<Film> findTop3ByGenereAndIdNot(String genere, Long currentId);

}
