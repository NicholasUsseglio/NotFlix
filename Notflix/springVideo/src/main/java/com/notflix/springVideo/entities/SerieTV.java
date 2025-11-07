package com.notflix.springVideo.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "serietv")
@PrimaryKeyJoinColumn(name = "id") //collega a intrattenimento
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)

public class SerieTV extends Intrattenimento {

    @ManyToOne
    @JoinColumn(
        name = "id_regista",
        referencedColumnName = "id"
    )

    private Regista regista;
    
    @Column(name = "trama")
    private String trama;
    @Column(name = "episodi")
    private Integer episodi;
    @Column(name = "numero_stagioni")
    private Integer numeroStagioni;
    @Column(name = "numero_episodi")
    private Integer numeroEpisodi;

     public SerieTV() {
        this.setTipo("serietv");
}

}
