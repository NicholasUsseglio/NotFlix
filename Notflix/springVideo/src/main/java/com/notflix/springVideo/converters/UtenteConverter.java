package com.notflix.springVideo.converters;

import org.springframework.stereotype.Component;

import com.notflix.springVideo.dto.UtenteDTO;
import com.notflix.springVideo.entities.Utente;

@Component
public class UtenteConverter implements GenericConverter<Utente, UtenteDTO> {

    @Override
    public Utente fromDToE(UtenteDTO dto) {

        Utente u = new Utente();
        u.setId(dto.getId());
        u.setNome(dto.getNome());
        u.setCognome(dto.getCognome());
        u.setDataNascita(dto.getDataNascita());
        u.setNazionalita(dto.getNazionalita());
        u.setUsername(dto.getUsername());
        u.setEmail(dto.getEmail());
        u.setRole(dto.getRole());
        return u;
    }

    @Override
public UtenteDTO fromEToD(Utente e) {

    UtenteDTO dto = new UtenteDTO();
    dto.setId(e.getId());
    dto.setNome(e.getNome());           // <-- aggiunto
    dto.setCognome(e.getCognome());     // <-- aggiunto
    dto.setDataNascita(e.getDataNascita()); // <-- aggiunto
    dto.setNazionalita(e.getNazionalita()); // <-- aggiunto
    dto.setUsername(e.getUsername());
    dto.setEmail(e.getEmail());
    dto.setRole(e.getRole());
    return dto;
}
}
