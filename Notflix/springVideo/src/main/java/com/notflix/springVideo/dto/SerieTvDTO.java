package com.notflix.springVideo.dto;

import java.sql.Date;

import com.notflix.springVideo.entities.Regista;

import lombok.Data;

@Data
public class SerieTvDTO implements GenericDTO {

    private Long id;
    private String titolo;
    private String genere;
    private Integer durata;
    private Date annoUscita;
    private double rating;
    private String trama;
    private Regista regista;
    private Integer episodi;
    private Integer numeroStagioni;
    private Integer numeroEpisodi;

    // 🔽 AGGIUNTA NECESSARIA
    // Serve per far funzionare il th:switch nel template HTML (th:case="'serietv'")
    // e per differenziare film / serie / podcast nel catalogo.
    private String tipo;

}
