package com.notflix.springVideo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.notflix.springVideo.entities.Preferito;

public interface PreferitoRepository extends JpaRepository<Preferito, Long> {

    List<Preferito> findByUtente_Id(Long idUtente);

    Optional<Preferito> findByUtente_IdAndIntrattenimento_Id(Long idUtente, Long idIntrattenimento);

    void deleteByUtente_IdAndIntrattenimento_Id(Long idUtente, Long idIntrattenimento);
}

