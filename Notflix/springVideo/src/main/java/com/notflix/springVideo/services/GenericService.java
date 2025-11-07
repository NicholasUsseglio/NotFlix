package com.notflix.springVideo.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import com.notflix.springVideo.converters.GenericConverter;
import com.notflix.springVideo.dto.GenericDTO;
import com.notflix.springVideo.entities.GenericEntity;

import lombok.Getter;

@Getter
public abstract class GenericService <
                                TipoID,
                                E extends GenericEntity,
                                D extends  GenericDTO,
                                C extends GenericConverter<E,D>,
                                R extends JpaRepository<E, TipoID>
                                > {
    
    
    
    @Autowired                           
    private R repository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private C converter;
    
    public List<D> getAll(){
        // grazie a JPARepository ho a disposizione alcuni metodi CRUD di base come "findAll()"
        List<E> lista = repository.findAll();
        // 1- scorro la lista di Enità             ----> (stream())
        // 2- converto ogni elemento               ----> (map(e -> {}))
        // 3- lo inserisco in una lista di DTO   ----> (toList())
        List<D> listaDTO = lista.stream().map(
            e -> {
                return converter.fromEToD(e);
            }
        ).toList();

        return listaDTO;
    }


    /*public E getById(TipoID id){
        return repository.findById(id).get();
    }*/
     public D getById(TipoID id){
        E e = repository.findById(id).get();
        D d = converter.fromEToD(e);
        return d;
    }


    public abstract E construct(Map<String, Object> entita);


    // save() effettua una insert o una update, per decidere controlla se l'id è già presente nel DB
    public boolean save(Map<String,Object> params){
        try{
            E e = construct(params);
            repository.save(e);
            return true;
        } catch (Exception ex){
            System.out.println("Errore nel metodo save di GenericService");
            return false;
        }
    }
   

    

    public void delete(TipoID id){
        try{
            repository.deleteById(id);
        } catch(Exception e){
            System.out.println("Errore nel metodo delete di GenericService");
        }
    }
    

}
