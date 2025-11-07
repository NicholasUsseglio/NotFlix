package com.notflix.springVideo.controller_rest;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notflix.springVideo.dto.AttoreDTO;
import com.notflix.springVideo.services.AttoreService;

import lombok.Data;

@RestController//@ResponseBody + @Controller
@RequestMapping("api/studente")
@Data
public class AttoriControllerRest {


    private final AttoreService attoreService;

    /*in un rest controller si possono gestire le richieste http usando 
     * le annotazioni @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
     * che indicano il tipo di richiesta http da gestire
     * e l'endpoint associato
     * cioè le API REST che il controller espone
     * link a delle risorse esterne:
     * https://medium.com/@shikha.ritu17/rest-api-architecture-6f1c3c99f0d3
     * https://www.geeksforgeeks.org/advance-java/spring-rest-controller/#
     * https://www.baeldung.com/spring-controller-vs-restcontroller
     */
    
    //questo metodo restituisce la lista di tutti gli studenti in formato json
    //l'oggetto restituito viene convertito automaticamente in json da spring boot
    //che quindi converte la lista di studentiDTO in un array json
    @GetMapping("/all")
    public List<AttoreDTO> getAll(){
        List<AttoreDTO> attori = attoreService.getAll();
        return attori;
    }

   
    @GetMapping("/all-attori")
    public ResponseEntity<List<AttoreDTO>> getAllAttori(){
        List<AttoreDTO> attori = attoreService.getAll(); //restituisce come risposta lo stato 200 OK, e il body con la lista 
        return ResponseEntity.ok().body(attori);     //restituisce un ResponseEntity, un oggetto che contiene corpo e stato, l'header etcc della risposta 

    }

    // restituisce uno attore dato il suo id, passato come parte del path dell'url
    //esempio di chiamata: /api/attore/byId/3
    //esempio di chiamata con RequestParam: /api/studente/byId?id=3
    
    //PathVariable indica che il valore viene preso dal path dell'url,a differenza di RequestParam che prende il valore dai parametri della query string
    //uso PathVariable perché la risorsa non è opzionale, per ottenere uno studente devo per forza specificare l'id
    @GetMapping("byId/{id}")
    public ResponseEntity<AttoreDTO> AttoreById(@PathVariable Long id){
        AttoreDTO s = attoreService.getById(id);
        if(s != null){
            return ResponseEntity.ok().body(s); //risposta 200 OK è lo stato, nel body della risposta c'è lo studente trovato
        }else{    //altrimenti restituisce una risposta 404 NOT FOUND, status della risposta indica che la risorsa non è stata trovata
         
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<Boolean> insertStudente(@RequestParam Map<String,Object> params){
        return ResponseEntity.ok().body(attoreService.save(params));
    }

    //metodo per cancellare uno studente
    @PostMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAttori(@PathVariable Long id){
        attoreService.delete(id);
        //restituisco una risposta senza corpo (void) con stato 204 NO CONTENT
        return ResponseEntity.noContent().build();
    }


}
