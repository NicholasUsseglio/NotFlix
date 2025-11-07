package com.notflix.springVideo.dto;

import java.sql.Date;
import java.util.List;

import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.entities.Attore;

import lombok.Data;

@Data
public class FilmDTO implements GenericDTO{

    private Long id;
    private String titolo;
    private String genere;
    private Integer durata;
    private Date annoUscita;
    private String rating;
    private String trama;
    private Regista regista;
    private List<Attore> attori;

}
