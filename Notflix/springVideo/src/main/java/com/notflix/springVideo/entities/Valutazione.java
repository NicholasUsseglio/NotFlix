package com.notflix.springVideo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Table(
    name = "valutazione",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_utente", "id_intrattenimento"})
    }
)
@Data
public class Valutazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_intrattenimento")
    private Intrattenimento intrattenimento;

    @Column(nullable = false, columnDefinition = "DECIMAL(2,1)")
    private Double voto; // 1.0..5.0 (allows half steps like 2.5)

    @Column(length = 2000)
    private String recensione;

    @Column(nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();
}
