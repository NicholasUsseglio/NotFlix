package com.notflix.springVideo.converters;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.RegistaDTO;
import com.notflix.springVideo.entities.Regista;

@Service
public class RegistaConverter implements GenericConverter<Regista, RegistaDTO> {

    @Override
    public Regista fromDToE(RegistaDTO dto) {
        Regista r = new Regista();
        r.setId(dto.getId());
        r.setNome(dto.getNome());
        r.setCognome(dto.getCognome());
        r.setDataNascita(dto.getDataNascita());
        r.setNazionalita(dto.getNazionalita());
        return r;
    }

    @Override
    public RegistaDTO fromEToD(Regista e) {
        RegistaDTO dto = new RegistaDTO();
        dto.setId(e.getId());
        dto.setNome(e.getNome());
        dto.setCognome(e.getCognome());
        dto.setDataNascita(e.getDataNascita());
        dto.setNazionalita(e.getNazionalita());
        return dto;
    }
    
}
