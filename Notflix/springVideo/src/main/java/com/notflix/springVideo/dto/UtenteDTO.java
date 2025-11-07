package com.notflix.springVideo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UtenteDTO implements GenericDTO {
    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String nazionalita;
    private String username;
    private String email;
    private String role;
}
