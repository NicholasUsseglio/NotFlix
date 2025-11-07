package com.notflix.springVideo.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.RegistaConverter;
import com.notflix.springVideo.dto.RegistaDTO;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.repositories.RegistaRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class RegistaService extends GenericService<Long, Regista, RegistaDTO, RegistaConverter, RegistaRepository> {
    
    //definisce come viene creata una istanza di tipo Regista
    @Override
    public Regista construct(Map<String, Object> entita) {
        return getContext().getBean(Regista.class, entita);
    }

    public boolean save(Map<String,Object> map){
        Regista r = construct(map);
        getRepository().save(r);
        return true;
    }

    public Page<Regista> getAllPages(int numeroPagina, int dimensione){
        Pageable pageable = PageRequest.of(numeroPagina, dimensione);
        return getRepository().findByNomeNotAndCognomeNot("Sconosciuto", "Sconosciuto", pageable);
    }

    public List<Regista> findByNameAndSurname(String nome, String cognome){
        return getRepository().findByNomeAndCognome(nome, cognome);
    }

    public List<Regista> findByName(String nome){
        return getRepository().findByNome(nome);
    }

    public List<Regista> findByCognome(String cognome){
        return getRepository().findByCognome(cognome);
    }

    public List<Regista> findByFilmografia(List<Intrattenimento> nome){
        return getRepository().findByFilmografia(nome);
    }

    public List<Regista> findByIdTitolo (String titolo){
        return getRepository().findByIdTitolo(titolo);
    }

    public List<Regista> findByNazionalita(String nazionalita){
        return getRepository().findByNazionalita(nazionalita);
    }

    public void delete(Long id){
        getRepository().deleteById(id);
    }
    
}
