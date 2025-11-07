package com.notflix.springVideo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.IntrattenimentoConverter;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.repositories.IntrattenimentoRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class IntrattenimentoService  {



    private final IntrattenimentoRepository intrattenimentoRepository;
    private final IntrattenimentoRepository repository;
    private final IntrattenimentoConverter converter;



    

    
    // --- Getter per il repository ---
    public IntrattenimentoRepository getRepository() {
        return repository;
    }

    // --- Getter per il converter ---
    public IntrattenimentoConverter getConverter() {
        return converter;
    }
    
    public Page<Intrattenimento> cercaGlobale (String q, Pageable pageable){
        return intrattenimentoRepository.cercaGlobale(q,pageable);

    }
}