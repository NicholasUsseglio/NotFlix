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
import lombok.EqualsAndHashCode;

@Entity
@Table(
    name = "watchlist",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_utente","id_intrattenimento"})
    }
)
@Data
@EqualsAndHashCode(callSuper = false)
public class Watchlist extends GenericEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_intrattenimento")
    private Intrattenimento intrattenimento;

    @Column(nullable = false)
    private LocalDateTime aggiuntoIl = LocalDateTime.now();
}
