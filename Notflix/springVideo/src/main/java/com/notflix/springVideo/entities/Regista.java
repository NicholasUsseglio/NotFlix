package com.notflix.springVideo.entities;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "registi")
@DiscriminatorValue("REGISTA")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class Regista extends Persona{
    
    //Viene ereditato l'id da Persona

   // @Column(name = "nazionalità")
   // private String nazionalita;

    //Filmografia contiene film e serie tv
    @OneToMany(mappedBy = "regista") //relazione 1-n con film e serie Tv
    private List<Film> filmografia;

     @ManyToMany(mappedBy = "regista")
    private List<SerieTV> serieTv;
    
}
