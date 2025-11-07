package com.notflix.springVideo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValutazioneDTO implements GenericDTO {

    private Long id;
    private Long idIntrattenimento;
    private String titoloIntrattenimento;
    private String tipo; // "film" o "serietv"
    private Double voto;
    private String recensione;
    private LocalDateTime dataCreazione;
}
