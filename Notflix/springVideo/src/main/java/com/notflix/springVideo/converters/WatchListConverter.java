package com.notflix.springVideo.converters;

import org.springframework.stereotype.Component;

import com.notflix.springVideo.dto.WatchListDTO;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Watchlist;

@Component
public class WatchListConverter implements GenericConverter<Watchlist, WatchListDTO> {

    @Override
    public Watchlist fromDToE(WatchListDTO dto) {
        return null; // non serve al momento
    }

    @Override
public WatchListDTO fromEToD(Watchlist watchlist) {
    Intrattenimento intrattenimento = watchlist.getIntrattenimento();
    String tipo;
    String trama = null;

    if (intrattenimento instanceof Film film) {  // Java 17 pattern matching
        tipo = "film";
        trama = film.getTrama();
    } else if (intrattenimento instanceof SerieTV serie) {
        tipo = "serietv";
        trama = serie.getTrama();
    } else {
        tipo = "sconosciuto";
    }

    return new WatchListDTO(
            watchlist.getId(),
            intrattenimento.getId(),
            intrattenimento.getTitolo(),
            tipo,
            trama,                  // ora ok
            watchlist.getAggiuntoIl()
    );
}
}