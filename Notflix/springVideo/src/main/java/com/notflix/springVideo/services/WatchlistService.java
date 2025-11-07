package com.notflix.springVideo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.entities.Watchlist;
import com.notflix.springVideo.repositories.IntrattenimentoRepository;
import com.notflix.springVideo.repositories.UtenteRepository;
import com.notflix.springVideo.repositories.WatchlistRepository;

@Service
public class WatchlistService {

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private IntrattenimentoRepository intrattenimentoRepository;

    @Transactional
    public boolean toggleWatchlist(Long idUtente, Long idIntrattenimento) {
        Optional<Watchlist> existing = watchlistRepository.findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento);

        if (existing.isPresent()) {
            watchlistRepository.deleteByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento);
            return false;
        } else {
            Utente u = utenteRepository.findById(idUtente).orElseThrow();
            Intrattenimento i = intrattenimentoRepository.findById(idIntrattenimento).orElseThrow();

            Watchlist w = new Watchlist();
            w.setUtente(u);
            w.setIntrattenimento(i);
            w.setAggiuntoIl(LocalDateTime.now());

            watchlistRepository.save(w);
            return true;
        }
    }

    public boolean isInWatchlist(Long idUtente, Long idIntrattenimento) {
        return watchlistRepository.findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento).isPresent();
    }

    public List<Watchlist> getWatchlistByUtente(Long idUtente) {
        return watchlistRepository.findByUtente_Id(idUtente);
    }

    public List<Watchlist> getFilmWatchlistByUtente(Long idUtente) {
        return getWatchlistByUtente(idUtente).stream()
                .filter(w -> w.getIntrattenimento() instanceof Film)
                .collect(Collectors.toList());
    }

    public List<Watchlist> getSerieWatchlistByUtente(Long idUtente) {
        return getWatchlistByUtente(idUtente).stream()
                .filter(w -> w.getIntrattenimento() instanceof SerieTV)
                .collect(Collectors.toList());
    }

    public List<Watchlist> findByUtente(Long idUtente) {
    return watchlistRepository.findByUtente_Id(idUtente);
}
}
