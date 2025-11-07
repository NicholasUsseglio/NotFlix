package com.notflix.springVideo.controllers;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.notflix.springVideo.dto.RegistaDTO;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.services.RegistaService;

import jakarta.servlet.http.HttpSession;



@Controller
public class RegistaController {

    private final RegistaService registaService;

    public RegistaController(RegistaService registaService) {
        this.registaService = registaService;
    }
    

    @GetMapping("/registi")
    public String registiPage(Model model){
        List<RegistaDTO> registi = registaService.getAll();
        if (registi == null) {
            registi = Collections.emptyList();
        }
        model.addAttribute("registiList", registi);

        return "registi";
    }

     @GetMapping("/cerca-regista")
    public String getAttoreByName(@RequestParam(required = false) String nome,
            @RequestParam(required = false) String cognome,
            Model model) {
        List<Regista> lista = registaService.findByNameAndSurname(nome, cognome);
        model.addAttribute("lista", lista);
        return "redirect:/registi";
    }

    @GetMapping("/elimina-regista")
    public String elimina(@RequestParam(defaultValue = "0L", required = true) Long idRegista, HttpSession session) {
        if(((String)session.getAttribute("role")).equalsIgnoreCase("ADMIN")){ //passo la session per la sicurezza//casto a stringa perché session ritorna degli object
            if (idRegista > 0) {
                registaService.delete(idRegista);
            }
        }
        return "redirect:/registi";
    }


    @PostMapping("/inserisci-regista")
    public String inserisci(@RequestParam Map<String,Object> map){
        registaService.save(map);
        return "redirect:/registi";
    }

    @PostMapping("/modifica-regista")
    public String aggiorna(@RequestParam Map<String,Object>map){
        registaService.save(map);
        return "redirect:/registi";
    }


}
