package com.notflix.springVideo.entities;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "intrattenimento")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true) // include id della superclasse nella equality
@ToString(callSuper = true)
public abstract class Intrattenimento extends GenericEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genere")
    private String genere;

    @Column(name = "titolo")
    private String titolo;

    @Column(name = "durata")
    private Integer durata;

    @Column(name = "anno_uscita")
    private Date annoUscita;

    @Column(name = "rating")
    private String rating;

    @Column(name = "tipo")
    private String tipo;
    

    @ManyToMany(mappedBy = "filmografia")
    private List<Attore> attori;


    
   
}
