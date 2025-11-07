package com.notflix.springVideo.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.notflix.springVideo.entities.Utente;

@Repository
public interface UtenteRepository extends JpaRepository <Utente, Long>{

    //metodi per la gestione di un utente user
    //SELECT p FROM Persona p WHERE p.username: username AND p.password :password) -> query in JPQL
    Utente findByUsernameAndPassword(String username, String password);

    Utente findByEmail (String email); //che è unique

    Utente findByUsername (String username); //prima cerco lo user, se non lo trova non cerco nemmeno la password e le credenziali sono errate.

   List<Utente> findByRole (String role); 
   
   boolean existsByUsername(String username);
   
   Utente save (Map <String, String> map); //salva user usando i dati di una mappa , quando prendo i dati dal form sotto forma di mappa 

   @Query ("UPDATE Utente p SET p.role =:role WHERE p.username=:username")
   Utente addRole (String username, String role);//aggiunge il ruolo allo user 

    // Ricerca utenti per nome, cognome, username, email o nazionalità
    List<Utente> findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNazionalitaContainingIgnoreCase(
        String nome, String cognome, String username, String email, String nazionalita
    );
}
   

   
