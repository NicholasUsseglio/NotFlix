package com.notflix.springVideo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notflix.springVideo.entities.Valutazione;

public interface ValutazioneRepository extends JpaRepository<Valutazione, Long> {

    // tutte le recensioni per uno specifico contenuto (film o serie o podcast)
    List<Valutazione> findByIntrattenimento_IdOrderByDataCreazioneDesc(Long idIntrattenimento);

    // valutazione fatta da un utente su quel contenuto
    Optional<Valutazione> findByUtente_IdAndIntrattenimento_Id(Long idUtente, Long idIntrattenimento);

    // media voto del contenuto
    @Query("SELECT AVG(v.voto) FROM Valutazione v WHERE v.intrattenimento.id = :idIntrattenimento")
    Double getMediaByIntrattenimento(@Param("idIntrattenimento") Long idIntrattenimento);

    long countByIntrattenimento_Id(Long idIntrattenimento);
}
