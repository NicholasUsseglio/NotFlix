package com.notflix.springVideo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity

@Table(name = "podcast")

@Inheritance(strategy = InheritanceType.JOINED)

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)

public class Podcast extends Intrattenimento{

    @Column(name="presentatore")
    private String presentatore;


    @Column(name="programmazioni")
    private String programmazioni;

    @Column(name="numero_episodi")
    private int numero_episodi;


}

    

