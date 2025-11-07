package com.notflix.springVideo.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Utente;
import com.notflix.springVideo.entities.Valutazione;
import com.notflix.springVideo.repositories.IntrattenimentoRepository;
import com.notflix.springVideo.repositories.UtenteRepository;
import com.notflix.springVideo.repositories.ValutazioneRepository;

import lombok.Data;

@Service
@Data
public class ValutazioneService {

    @Autowired
    private ValutazioneRepository valutazioneRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private IntrattenimentoRepository intrattenimentoRepository; // nuovo repo, vedi sotto

    public void salvaValutazione(Long idUtente, Long idIntrattenimento, Double voto, String recensione) {
        Valutazione v = valutazioneRepository
                .findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento)
                .orElse(new Valutazione());

        Utente u = utenteRepository.findById(idUtente).orElseThrow();
        Intrattenimento i = intrattenimentoRepository.findById(idIntrattenimento).orElseThrow();

        v.setUtente(u);
        v.setIntrattenimento(i);
        // clamp voto between 1.0 and 5.0
        double vv = (voto == null) ? 1.0 : voto;
        if (Double.isNaN(vv) || Double.isInfinite(vv)) vv = 1.0;
        if (vv < 1.0) vv = 1.0;
        if (vv > 5.0) vv = 5.0;
        v.setVoto(vv);
        v.setRecensione(recensione);
        v.setDataCreazione(LocalDateTime.now());

        valutazioneRepository.save(v);
    }

    public List<Valutazione> getRecensioni(Long idIntrattenimento) {
        return valutazioneRepository.findByIntrattenimento_IdOrderByDataCreazioneDesc(idIntrattenimento);
    }

    public Double getMedia(Long idIntrattenimento) {
        Double media = valutazioneRepository.getMediaByIntrattenimento(idIntrattenimento);
        return media != null ? media : 0.0;
    }

    public long getNumeroRecensioni(Long idIntrattenimento) {
        return valutazioneRepository.countByIntrattenimento_Id(idIntrattenimento);
    }

    public Optional<Valutazione> getValutazioneUtente(Long idUtente, Long idIntrattenimento) {
        return valutazioneRepository.findByUtente_IdAndIntrattenimento_Id(idUtente, idIntrattenimento);
    }

    public List<Valutazione> getRecensioniUtente(Long idUtente) {
    return valutazioneRepository.findAll()
        .stream()
        .filter(v -> v.getUtente().getId().equals(idUtente))
        .toList();
}
}
