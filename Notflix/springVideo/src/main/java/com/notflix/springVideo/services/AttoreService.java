package com.notflix.springVideo.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.notflix.springVideo.converters.AttoreConverter;
import com.notflix.springVideo.dto.AttoreDTO;
import com.notflix.springVideo.entities.Attore;

import com.notflix.springVideo.repositories.AttoreRepository;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Service
@Data
@EqualsAndHashCode(callSuper=false)
public class AttoreService extends GenericService<Long, Attore, AttoreDTO,AttoreConverter, AttoreRepository> {
    
    //definisce come viene creata una istanza di tipo Studente
    @Override
    public Attore construct(Map<String, Object> entita) {
        return getContext().getBean(Attore.class, entita);
    }

    
    /*public Studente save(Map<String, String> map) {
        Studente s=construct(map); //converto la mappa in ingresso in oggetto
        return getRepository().save(s); //salvo lo studente
*/

    
    public Page<Attore> getAllPages(int numeroPagina, int dimensione){
        Pageable pageable =PageRequest.of(numeroPagina, dimensione);

        return getRepository().findAllPageable(pageable);
    }

 //studenti per nominativo
    public List<Attore> getByNameAndSurname(String nome,String cognome){
        
        return getRepository().findByNomeAndCognome(nome, cognome);
    }


    //metodo che cerca una un film per attore 
    //o un attore per film 
    public List<Attore> getByFilm(String titolo){
        return getRepository().findByFilm(titolo);
    }

    //metodo che cerca una un serie per attore 
    //o un attore per serie 
    
      public List<Attore> getBySerieTv(String titolo){
        return getRepository().findBySerieTv(titolo);
    }

    public Optional<AttoreDTO> findByIdSafe(Long id) {
    try {
        return getRepository().findById(id)
                .map(entity -> getConverter().toDto(entity));
    } catch (Exception e) {
        return Optional.empty();
    }
}

}
