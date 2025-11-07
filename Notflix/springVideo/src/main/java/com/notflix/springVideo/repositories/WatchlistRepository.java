package com.notflix.springVideo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notflix.springVideo.entities.Watchlist;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUtente_Id(Long idUtente);
    Optional<Watchlist> findByUtente_IdAndIntrattenimento_Id(Long idUtente, Long idIntrattenimento);
    void deleteByUtente_IdAndIntrattenimento_Id(Long idUtente, Long idIntrattenimento);
}
