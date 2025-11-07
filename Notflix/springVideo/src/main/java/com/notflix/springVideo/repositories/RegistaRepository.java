package com.notflix.springVideo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.entities.Intrattenimento;


public interface RegistaRepository extends JpaRepository<Regista, Long>{
    
    @Query("SELECT r FROM Regista r WHERE r.nome != 'Sconosciuto' AND r.cognome != 'Sconosciuto'")
    List<Regista> findAll();

    List<Regista> findByNomeAndCognome(String nome, String cognome);

    List<Regista> findByNome(String nome);

    List<Regista> findByCognome(String cognome);

    List<Regista> findByFilmografia(List<Intrattenimento> filmografia);

    List<Regista> findByNazionalita(String nazionalità);

    @Query("SELECT r FROM Regista r JOIN r.filmografia i WHERE i.titolo = :titolo")
    List<Regista> findByIdTitolo(@Param("titolo") String titolo);

    @Override
    void delete(Regista entity);

    @Query("SELECT r FROM Regista r WHERE r.nome != :nome AND r.cognome != :cognome")
    Page<Regista> findByNomeNotAndCognomeNot(@Param("nome") String nome, @Param("cognome") String cognome, Pageable pageable);

}
