package com.notflix.springVideo.controller_rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notflix.springVideo.dto.RegistaDTO;
import com.notflix.springVideo.entities.Intrattenimento;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.services.RegistaService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/registi")
public class RegistaControllerRest {

    private final RegistaService registaService;

    @Autowired
    public RegistaControllerRest(RegistaService registaService){
        this.registaService = registaService;
    }

    @GetMapping
    public ResponseEntity<List<Regista>> getAll() {
        return ResponseEntity.ok(registaService.getRepository().findAll());
    }
    
    @GetMapping("byId/{Id}")
    public ResponseEntity<RegistaDTO> getById(@PathVariable Long id) {
        RegistaDTO regista = registaService.getById(id);
        if (regista != null) {
            return ResponseEntity.ok().body(regista);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<Regista> create(@RequestBody Regista regista) {
        Regista saved = registaService.getRepository().save(regista);     
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<Regista> update(@PathVariable Long id,
                                        @RequestBody Regista regista) {
        if (!registaService.getRepository().existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        regista.setId(id);
        Regista updated = registaService.getRepository().save(regista);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> delete(@PathVariable Long Id){
        if (!registaService.getRepository().existsById(Id)) {
            return ResponseEntity.notFound().build();
        }
        registaService.getRepository().deleteById(Id);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Regista>> search(@RequestParam(required = false) String nome,
                                                @RequestParam(required = false) String cognome,
                                                @RequestParam(required = false) String nazionalita) {
        if (nome != null) {
            return ResponseEntity.ok(registaService.getRepository().findByNome(nome));
        }
        if (cognome != null) {
            return ResponseEntity.ok(registaService.getRepository().findByCognome(cognome));
        }
        if (nazionalita != null) {
            return ResponseEntity.ok(registaService.getRepository().findByNazionalita(nazionalita));
        }
        return ResponseEntity.badRequest().build();
    }


    @PostMapping("/search/by-filmografia")
    public ResponseEntity<List<Regista>> searchByFilmografia(@RequestBody List<Intrattenimento> intrattenimento) {        
        return ResponseEntity.ok(registaService.getRepository().findByFilmografia(intrattenimento));
    }
    

    
    
    
    
}
