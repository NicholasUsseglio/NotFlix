package com.notflix.springVideo.converters;

import org.springframework.stereotype.Component;
import com.notflix.springVideo.dto.ValutazioneDTO;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Valutazione;

@Component
public class ValutazioneConverter {

    public ValutazioneDTO fromEToD(Valutazione v) {
        Intrattenimento i = v.getIntrattenimento();
        String tipo;
        if (i instanceof Film) tipo = "film";
        else if (i instanceof SerieTV) tipo = "serietv";
        else tipo = "sconosciuto";

        return new ValutazioneDTO(
            v.getId(),
            i.getId(),
            i.getTitolo(),
            tipo,
            v.getVoto(),
            v.getRecensione(),
            v.getDataCreazione()
        );
    }
}
