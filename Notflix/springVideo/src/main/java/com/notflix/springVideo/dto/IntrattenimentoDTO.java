package com.notflix.springVideo.dto;

import java.sql.Date;


import lombok.Data;

@Data
public class IntrattenimentoDTO implements GenericDTO{

    private Long id;
    private String titolo;
    private String genere;
    private Integer durata;
    private Date annoUscita;
    private String rating;
    private String tipo;
    
}
