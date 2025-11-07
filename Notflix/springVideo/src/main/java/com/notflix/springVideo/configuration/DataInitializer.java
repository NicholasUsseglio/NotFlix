package com.notflix.springVideo.configuration;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.repositories.FilmRepository;
import com.notflix.springVideo.repositories.RegistaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(FilmRepository filmRepository, RegistaRepository registaRepository) {
        return args -> {
            // Controlla se ci sono già dati nel database
            System.out.println("Checking if database needs initialization...");
            if (filmRepository.count() == 0) {
                System.out.println("Database is empty, initializing with sample data...");
                // Crea un regista
                Regista regista = new Regista();
                regista.setNome("Christopher");
                regista.setCognome("Nolan");
                regista.setNazionalita("British-American");
                regista = registaRepository.save(regista);
                System.out.println("Regista created with ID: " + regista.getId());

                // Crea un film
                Film inception = new Film();
                inception.setTitolo("Inception");
                inception.setGenere("Sci-Fi");
                inception.setTrama("Un ladro esperto nel rubare segreti dalla mente delle persone durante il sonno deve tentare l'impossibile: l'inception.");
                inception.setDurata(148);
                inception.setAnnoUscita(Date.valueOf("2010-07-16")); // 2010-07-16
                inception.setRating("8.8");
                inception.setRegista(regista);
                inception.setAttori(new ArrayList<>()); // inizializza la lista degli attori vuota
                filmRepository.save(inception);

                // Crea un altro film
                Film interstellar = new Film();
                interstellar.setTitolo("Interstellar");
                interstellar.setGenere("Sci-Fi");
                interstellar.setTrama("Un gruppo di astronauti intraprende un viaggio attraverso un wormhole alla ricerca di una nuova casa per l'umanità.");
                interstellar.setDurata(169);
                interstellar.setAnnoUscita(Date.valueOf("2014-11-07")); // 2014-11-07
                interstellar.setRating("8.6");
                interstellar.setRegista(regista);
                interstellar.setAttori(new ArrayList<>()); // inizializza la lista degli attori vuota
                filmRepository.save(interstellar);
            }
        };
    }
}