package com.notflix.springVideo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.PreferitoConverter;
import com.notflix.springVideo.converters.UtenteConverter;
import com.notflix.springVideo.converters.WatchListConverter;
import com.notflix.springVideo.dto.UtenteDTO;
import com.notflix.springVideo.entities.Persona;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.entities.Valutazione;
import com.notflix.springVideo.repositories.IntrattenimentoRepository;
import com.notflix.springVideo.repositories.UtenteRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;



@Service
@Data
@EqualsAndHashCode(callSuper=false)
public class UtenteService extends GenericService<
        Long,
        Utente,
        UtenteDTO,
        UtenteConverter,
        UtenteRepository> {

    @Autowired
    private PreferitoConverter preferitoConverter;

    public PreferitoConverter getPreferitoConverter() {
        return preferitoConverter;
    }

    @Autowired
    private ValutazioneService valutazioneService; // inietta il servizio recensioni

    public List<Valutazione> getRecensioniUtente(Long idUtente) {
    return valutazioneService.getRecensioniUtente(idUtente);
    }

    @Autowired
    private WatchListConverter watchlistConverter;

    public WatchListConverter getWatchlistConverter() {
    return watchlistConverter;
}

    @Autowired
    private IntrattenimentoRepository intrattenimentoRepository;

    public IntrattenimentoRepository getIntrattenimentoRepository() {
        return intrattenimentoRepository;
    }


      public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }
    
    @Override
    public Utente construct(Map<String, Object> mappa) {
    Utente u = getContext().getBean(Utente.class, mappa);

    if (mappa.containsKey("id")) {
        Object idValue = mappa.get("id");
        if (idValue != null) {
            u.setId(Long.parseLong(idValue.toString()));
        }
    }

    if (mappa.containsKey("username")) u.setUsername((String) mappa.get("username"));
    if (mappa.containsKey("email")) u.setEmail((String) mappa.get("email"));
    if (mappa.containsKey("role")) u.setRole((String) mappa.get("role"));
    if (mappa.containsKey("nome")) u.setNome((String) mappa.get("nome"));
    if (mappa.containsKey("cognome")) u.setCognome((String) mappa.get("cognome"));
    if (mappa.containsKey("nazionalita")) u.setNazionalita((String) mappa.get("nazionalita"));

    if (mappa.containsKey("dataNascita") && mappa.get("dataNascita") != null) {
        Object dataValue = mappa.get("dataNascita");
        if (dataValue instanceof LocalDate) {
            u.setDataNascita((LocalDate) dataValue);
        } else {
            u.setDataNascita(LocalDate.parse(dataValue.toString()));
        }
    }

    if (mappa.containsKey("password")) u.setPassword((String) mappa.get("password"));

    return u;
}


public boolean save(Map<String, Object> dati) {
        Utente utente;

    if (dati.containsKey("id")) {
        Long id = Long.parseLong(dati.get("id").toString());
        utente = utenteRepository.findById(id).orElse(null);
        if (utente == null) return false;
    } else {
        utente = new Utente(); // nuovo utente
    }

    if (dati.containsKey("username")) utente.setUsername((String) dati.get("username"));
    if (dati.containsKey("email")) utente.setEmail((String) dati.get("email"));
    if (dati.containsKey("role")) utente.setRole((String) dati.get("role"));
    if (dati.containsKey("nome")) utente.setNome((String) dati.get("nome"));
    if (dati.containsKey("cognome")) utente.setCognome((String) dati.get("cognome"));
    if (dati.containsKey("nazionalita")) utente.setNazionalita((String) dati.get("nazionalita"));
    if (dati.containsKey("dataNascita") && dati.get("dataNascita") != null) {
        Object dataValue = dati.get("dataNascita");
        if (dataValue instanceof LocalDate) {
            utente.setDataNascita((LocalDate) dataValue);
        } else {
            utente.setDataNascita(LocalDate.parse(dataValue.toString()));
        }
    }
    if (dati.containsKey("password")) 
    utente.setPassword((String) dati.get("password"));


    utenteRepository.save(utente);
    return true;
}

    public UtenteDTO getByUsername(String username) {
        Persona p = getRepository().findByUsername(username);
        if (p instanceof Utente) {
            return getConverter().fromEToD((Utente) p);
        }
        return null;
    }

    public UtenteDTO getByEmail(String email) {
        Persona p = getRepository().findByEmail(email);
        if (p instanceof Utente) {
            return getConverter().fromEToD((Utente) p);
        }
        return null;
    }

    public List<Utente> getByRole(String role) {
        List<Utente> persone = getRepository().findByRole(role);
        return persone.stream()
                .filter(p -> p instanceof Utente)
                .map(p -> (Utente) p)
                .collect(Collectors.toList());
    }

    public UtenteDTO getByUsernameAndPassword(String username, String password) {
        Persona p = getRepository().findByUsernameAndPassword(username, password);
        if (p instanceof Utente) {
            return getConverter().fromEToD((Utente) p);
        }
        return null;
    }

    public void addRole(String username, String role) {
        getRepository().addRole(username, role);
    }

    
    @Service
    public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = (Utente) utenteRepository.findByUsername(username);

         if (utente == null) {
            throw new UsernameNotFoundException("Utente non trovato");
        }
        
        // Determiniamo il ruolo da assegnare
        String role = utente.getRole();
         if (role == null || role.isEmpty() || !role.equals("ADMIN")) {
            role = "USER"; // Tutti i non-ADMIN sono USER
        }

        // Se l'utente è ADMIN, gli assegniamo SOLO ADMIN.
        // Se è un altro ruolo (es. USER), gli assegniamo quello.
        // Questo impedisce l'assegnazione implicita del doppio ruolo.
        return User.builder()
            .username(utente.getUsername())
            .password(utente.getPassword()) // in chiaro, se usi NoOpPasswordEncoder
            .roles(role) 
            .build();
    }
}

@Autowired
    
    private UtenteRepository utenteRepository;
  public boolean usernameExists(String username) {
        return utenteRepository.existsByUsername(username);
    }

   public List<Utente> searchUsers(String keyword) {
    return utenteRepository.findByNomeContainingIgnoreCaseOrCognomeContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrNazionalitaContainingIgnoreCase(
        keyword, keyword, keyword, keyword, keyword
        );
    }

    public void deleteById(Long id) {
        utenteRepository.deleteById(id);
    }
}
