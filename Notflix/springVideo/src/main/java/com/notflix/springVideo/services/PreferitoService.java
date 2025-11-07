package com.notflix.springVideo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.notflix.springVideo.converters.PreferitoConverter;
import com.notflix.springVideo.dto.PreferitoDTO;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Preferito;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.repositories.IntrattenimentoRepository;
import com.notflix.springVideo.repositories.PreferitoRepository;
import com.notflix.springVideo.repositories.UtenteRepository;

@Service
public class PreferitoService {

    @Autowired
    private PreferitoRepository preferitoRepository;

    @Autowired
    private PreferitoConverter preferitoConverter;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private IntrattenimentoRepository intrattenimentoRepository;

    @Transactional
    public boolean togglePreferito(Long idUtente, Long idIntrattenimento) {
        Optional<Preferito> existing =
                preferitoRepository.findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento);

        if (existing.isPresent()) {
            preferitoRepository.deleteByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento);
            return false;
        } else {
            Utente u = utenteRepository.findById(idUtente).orElseThrow();
            Intrattenimento i = intrattenimentoRepository.findById(idIntrattenimento).orElseThrow();

            Preferito p = new Preferito();
            p.setUtente(u);
            p.setIntrattenimento(i);
            p.setAggiuntoIl(LocalDateTime.now());

            preferitoRepository.save(p);
            return true;
        }
    }

    public boolean isPreferito(Long idUtente, Long idIntrattenimento) {
        return preferitoRepository
                .findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento)
                .isPresent();
    }

    // ✅ Ottiene solo i Film preferiti
public List<PreferitoDTO> getFilmPreferiti(Long idUtente) {
    return preferitoRepository.findByUtente_Id(idUtente)
            .stream()
            .filter(p -> {
                Intrattenimento i = p.getIntrattenimento();
                if (i == null || i.getTipo() == null) return false;
                String tipo = i.getTipo().trim().toLowerCase();
                return tipo.contains("film"); // accetta "Film", "film", "FILM", ecc.
            })
            .map(preferitoConverter::fromEToD)
            .collect(Collectors.toList());
}

// ✅ Ottiene solo le Serie TV preferite
public List<PreferitoDTO> getSeriePreferite(Long idUtente) {
    return preferitoRepository.findByUtente_Id(idUtente)
            .stream()
            .filter(p -> {
                Intrattenimento i = p.getIntrattenimento();
                if (i == null || i.getTipo() == null) return false;
                String tipo = i.getTipo().trim().toLowerCase();
                return tipo.contains("serie"); // accetta "SerieTV", "Serie TV", "serie", ecc.
            })
            .map(preferitoConverter::fromEToD)
            .collect(Collectors.toList());
}
}
