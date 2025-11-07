package com.notflix.springVideo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListDTO implements GenericDTO {

    private Long id;
    private Long idIntrattenimento; // id reale del Film o SerieTV
    private String titoloIntrattenimento;
    private String tipo; // "film" o "serietv"
    private String trama;
    private LocalDateTime aggiuntoIl;
}
