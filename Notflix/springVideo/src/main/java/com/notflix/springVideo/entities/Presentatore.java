package com.notflix.springVideo.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "registi")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper = true)
public class Presentatore extends Persona {

    @OneToMany(mappedBy = "presentatore") //relazione 1-n con film e serie Tv
    private List<Podcast> filmografia;


}
