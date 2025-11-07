package com.notflix.springVideo.services;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.SerieTvConverter;
import com.notflix.springVideo.dto.SerieTvDTO;
import com.notflix.springVideo.entities.Attore;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.repositories.SerieTvRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper = false)
public class SerieTvService extends GenericService<Long, SerieTV, SerieTvDTO, SerieTvConverter, SerieTvRepository> {

    private final SerieTvRepository serieTVRepository;

    private final SerieTvConverter serietvConverter;

    /*public Page<SerieTV> getAll(Pageable pageable) {
    return serieTVRepository.findAllSerieTv(pageable);
}
    //questi 3 erano attivi:
    public Page<SerieTV> getAll (Pageable pageable) {
    return serieTVRepository.findByGenereLike("%TV%", pageable);
    }
    
    public List<SerieTV> getByGenere(String genere) {
        return getRepository().findByGenereIgnoreCase(genere);
    }
    
    public Page<SerieTV> getByGenere(String genere, Pageable pageable) {
        return serieTVRepository.findByGenereIgnoreCase(genere, pageable);
    }

*/

    /*public Page<SerieTV> getAll(Pageable pageable) {
    return serieTVRepository.getAll(pageable);
}

public Page<SerieTV> getByGenere(String genere, Pageable pageable) {
    return serieTVRepository.getByGenere(genere, pageable);
}*/

 public Page<SerieTV> getAll(Pageable pageable) {
    return serieTVRepository.getAllSeries(pageable);
}

public Page<SerieTV> getByGenere(String genere, Pageable pageable) {
    return serieTVRepository.getByGenere(genere, pageable);
}





    public Page<SerieTV> cercaPerTitoloOPerAttore (String q, Pageable pageable){
        return serieTVRepository.cercaPerTitoloOPerAttore(q,pageable);

    }
   

    public List<SerieTV> getByTitolo(String titolo) {
        return getRepository().findByTitolo(titolo);
    }

    public List<SerieTV> getByRegista(Regista regista) {
        return getRepository().findByRegista(regista);
    }

    // Cerca per singolo attore
    public List<SerieTV> getByAttore(Attore attore) {
        return getRepository().findByAttori(Arrays.asList(attore));
    }

    // Cerca per lista di attori
    public List<SerieTV> getByAttori(List<Attore> attori) {
        return getRepository().findByAttori(attori);
    }

    public List<SerieTV> getByAnnoUscita(Date annoUscita) {
        return getRepository().findByAnnoUscita(annoUscita);
    }


    public List<SerieTV> findByRangeRating(double minRating, double maxRating) {
        return getRepository().findByRangeRating(minRating, maxRating);
    }

    public SerieTV construct(Map<String, Object> entita) {
        SerieTV serieTV = getContext().getBean(SerieTV.class, entita);
        return serieTV;
    }

    // Recupera l'entità SerieTV
    public Optional<SerieTV> findByIdSafe(Long id) {
        return getRepository().findById(id);
    }
/*LO COMMETTO, E METTO VERSIONE UNIFICATA CON PRECEDENTE METODO FUNZIONANTE PER IL FILTRO
    // Recupera il DTO sicuro (per controller)
       public Optional<SerieTvDTO> findDtoByIdSafe(Long id) {
        try {
        // Ottiene il DTO base tramite il metodo del GenericService
        SerieTvDTO dto = this.getById(id);
        if (dto == null) return Optional.empty();

        // 🔧 Recupera l'entità completa per riempire i campi eventualmente null
        getRepository().findById(id).ifPresent(entity -> {
            if (Objects.isNull(dto.getTrama()))
                dto.setTrama(entity.getTrama());
            if (Objects.isNull(dto.getNumeroStagioni()))
                dto.setNumeroStagioni(entity.getNumeroStagioni());
            if (Objects.isNull(dto.getNumeroEpisodi()))
                dto.setNumeroEpisodi(entity.getNumeroEpisodi());
            if (Objects.isNull(dto.getEpisodi()))
                dto.setEpisodi(entity.getEpisodi());
            if (Objects.isNull(dto.getRegista()))
                dto.setRegista(entity.getRegista());
        });

        return Optional.of(dto);
    } catch (Exception e) {
        e.printStackTrace(); // stampa eventuali errori nel log
        return Optional.empty();
    }
}
*/
public Optional<SerieTvDTO> findDtoByIdSafe(Long id) {
    try {
        // 🔹 Recupera il DTO base tramite GenericService
        SerieTvDTO dto = this.getById(id);
        if (dto == null) {
            return Optional.empty(); // nessun DTO trovato
        }

        // 🔹 Recupera l'entità completa per completare eventuali campi null
        getRepository().findById(id).ifPresent(entity -> {
            if (dto.getTrama() == null) dto.setTrama(entity.getTrama());
            if (dto.getNumeroStagioni() == null) dto.setNumeroStagioni(entity.getNumeroStagioni());
            if (dto.getNumeroEpisodi() == null) dto.setNumeroEpisodi(entity.getNumeroEpisodi());
            if (dto.getEpisodi() == null) dto.setEpisodi(entity.getEpisodi());
            if (dto.getRegista() == null) dto.setRegista(entity.getRegista());
        });

        return Optional.of(dto);
    } catch (Exception e) {
        e.printStackTrace();
        return Optional.empty();
    }
}
    /**
     * Legge il nome del regista direttamente dal DB (join serieTV->registi->persone)
     * Restituisce null se non trovato.
     */
    public String getDirectorNameFromDb(Long serieTVId) {
        try {
            Object[] r = getRepository().findDirectorNameBySerietvId(serieTVId);
            if (r != null && r.length >= 2) {
                String n = r[0] != null ? r[0].toString() : null;
                String c = r[1] != null ? r[1].toString() : null;
                if ((n != null && !n.isBlank()) || (c != null && !c.isBlank())) {
                    String name = (n != null ? n : "") + " " + (c != null ? c : "");
                    return name.trim();
                }
            }
        } catch (Exception ex) {
            // ignora e ritorna null
        }
        return null;
    }

   // public Page<SerieTV> getAll(Pageable pageable) {
    //    return serieTVRepository.findAll(pageable);    }

    public Page<SerieTV> searchByKeyword(String keyword, Pageable pageable) {
    return serieTVRepository.findByTitoloContainingIgnoreCase(keyword,pageable);
}

    // Metodo per trovare i correlati
    public List<SerieTvDTO> findTop3CorrelateByGenere(Long currentId, String genere) {
        if (genere == null || genere.isBlank() || currentId == null) {
            return Collections.emptyList();
        }
        
        // 1. Chiama il repository per recuperare le Entità SerieTV
        // Assumendo che tu abbia implementato SerieTvRepository come suggerito
        List<SerieTV> entities = serieTVRepository.findTop3ByGenereAndIdNot(genere, currentId);
        
        // 2. Mappa le Entity in DTO utilizzando il tuo Converter
        return entities.stream()
                .map(serietvConverter::fromEToD) // <-- UTILIZZO DEL TUO CONVERTER
                .collect(Collectors.toList());
    }
}
