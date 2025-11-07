package com.notflix.springVideo.converters;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.dto.AttoreDTO;
import com.notflix.springVideo.entities.Attore;

@Service
public class AttoreConverter implements GenericConverter<Attore, AttoreDTO>{    

    @Override
    public Attore fromDToE(AttoreDTO dto) {
        Attore a=new Attore ();
        a.setId(dto.getId());
        a.setNome(dto.getNome());
        a.setCognome(dto.getCognome());
        a.setDataNascita(dto.getData_nascita());
        a.setNazionalita(dto.getNazionalita());
        a.setFilmografia(dto.getFilmografia());
        return a;
    }

    @Override
    public AttoreDTO fromEToD(Attore e) {
       AttoreDTO dto= new AttoreDTO();
       dto.setId(e.getId());
       dto.setNome(e.getNome());
       dto.setCognome(e.getCognome());
       dto.setData_nascita(e.getDataNascita());
       dto.setNazionalita(e.getNazionalita());
       dto.setFilmografia(e.getFilmografia());
       return dto;

    }

    // 🔹 Aggiungi alias compatibili con il GenericService
    public AttoreDTO toDto(Attore e) {
        return fromEToD(e);
    }

    public Attore toEntity(AttoreDTO dto) {
        return fromDToE(dto);
    }
    
}
