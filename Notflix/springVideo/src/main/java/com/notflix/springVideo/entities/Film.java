package com.notflix.springVideo.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/*
 * create table film (
id bigint primary key (auto_increment),
trama varchar(500),
id_regista bigint,
id_intrattenimento bigint,
foreign key (id_regista) references  registi(id),
foreign key (id_intrattenimento) references  intrattenimento(id)

);
 */

@Entity
@Table(name = "film")

@PrimaryKeyJoinColumn(name = "id") //collega a intrattenimento
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class Film extends Intrattenimento{

    @ManyToOne
    @JoinColumn(
        name = "id_regista",
        referencedColumnName = "id"
    )

    private Regista regista;

    @Column
    private String trama;

     @ManyToMany(mappedBy = "filmografia")
    private List<Attore> attori;

      public Film() {
        this.setTipo("film");
      }

    
    

}
