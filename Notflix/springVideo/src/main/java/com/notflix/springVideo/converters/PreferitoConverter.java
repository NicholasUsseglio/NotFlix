package com.notflix.springVideo.converters;

import org.springframework.stereotype.Component;
import com.notflix.springVideo.dto.PreferitoDTO;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Preferito;
import com.notflix.springVideo.entities.SerieTV;

@Component
public class PreferitoConverter implements GenericConverter<Preferito, PreferitoDTO> {

    @Override
    public Preferito fromDToE(PreferitoDTO dto) {
        return null; // non serve al momento
    }

    @Override
public PreferitoDTO fromEToD(Preferito preferito) {
    Intrattenimento intrattenimento = preferito.getIntrattenimento();
    String tipo;
    String trama = null;

    if (intrattenimento instanceof Film) {
        tipo = "film";
        trama = ((Film) intrattenimento).getTrama();
    } else if (intrattenimento instanceof SerieTV) {
        tipo = "serietv";
        trama = ((SerieTV) intrattenimento).getTrama();
    } else {
        tipo = "sconosciuto";
    }

    return new PreferitoDTO(
            preferito.getId(),
            intrattenimento.getId(), // <-- questo è fondamentale
            intrattenimento.getTitolo(),
            tipo,
            trama,
            preferito.getAggiuntoIl()
    );
}
}
