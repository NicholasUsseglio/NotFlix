package com.notflix.springVideo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegistaDTO implements GenericDTO{

    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String nazionalita;

}