package com.notflix.springVideo.entities;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;



@Entity
@Table(name="attori")
@DiscriminatorValue("ATTORE")

@Data
@EqualsAndHashCode(callSuper=false)
@ToString (callSuper = true)
public class Attore extends Persona {

    //id lo eredita da persona


   // @Column(name="nazionalita")
   // private String nazionalita;

    //filmografia (cast per la classe intrattenimento) 
    @ManyToMany //n-attori e n-film o serie
    @JoinTable (
        name="cast_attori",
        joinColumns= {@JoinColumn(name="id_attore")}, 
        inverseJoinColumns= {@JoinColumn(name="id_intrattenimento")}
    )
    private List<Intrattenimento> filmografia;    
}
