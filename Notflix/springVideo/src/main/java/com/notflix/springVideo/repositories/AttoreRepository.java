package com.notflix.springVideo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.notflix.springVideo.entities.Attore;



@Repository
public interface AttoreRepository extends JpaRepository <Attore, Long>{

    //metodo che cerca gli attori per nome
    List<Attore> findByNome(String nome); 

    //metodo che cerca per nome e cognome
    //SELECT s FROM attori WHERE s.nome= :nome AND s.cognome= :cognome;
   List<Attore> findByNomeAndCognome(String nome, String cognome);

    List<Attore> findAll();

    //restituisce un page studente 
    @Query("SELECT a FROM Attore a")
    Page<Attore> findAllPageable (Pageable pageable);


    //metodo che cerca per nazionalità
     List<Attore> findByNazionalita(String nazionalita);


    //metodo che cerca Attore per film o serie 

    //@Query("SELECT a FROM Attore a JOIN a.intrattenimenti i JOIN Film f ON f.intrattenimento = i WHERE f.titolo= :film")
    @Query("SELECT a FROM Attore a JOIN a.filmografia i WHERE TYPE(i) = Film AND LOWER(i.titolo) = LOWER(:titolo)")
    List<Attore> findByFilm(@Param("titolo") String titolo);

    //@Query("SELECT a FROM Attore a JOIN a.intrattenimenti i JOIN SerieTv st ON st.intrattenimento = i WHERE st.titolo= :serieTv")
    @Query("SELECT a FROM Attore a JOIN a.filmografia i WHERE TYPE(i) = SerieTV AND LOWER(i.titolo) = LOWER(:titolo)")
    List<Attore> findBySerieTv(@Param("titolo") String titolo);
   
}
