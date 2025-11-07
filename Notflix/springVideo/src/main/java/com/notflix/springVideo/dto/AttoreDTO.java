package com.notflix.springVideo.dto;

import java.time.LocalDate;
import java.util.List;

import com.notflix.springVideo.entities.Intrattenimento;

import lombok.Data;

@Data
public class AttoreDTO implements GenericDTO{
    private Long id;
    private String nome;
    private String cognome;
    private LocalDate data_nascita;
    private String nazionalita;   
    private List<Intrattenimento> filmografia;    
//la lascerei la filmografia no?
    
}
