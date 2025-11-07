package com.notflix.springVideo.entities;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import jakarta.persistence.DiscriminatorType;


@Entity
@Table(name = "persone")


@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)

public class Persona extends GenericEntity{

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(name="nome")
    private String nome;

    @Column(name="cognome")
    private String cognome;

    @Column(name="data_nascita")
    private LocalDate dataNascita;

    @Column(name = "nazionalità")
    private String nazionalita;



    

}
