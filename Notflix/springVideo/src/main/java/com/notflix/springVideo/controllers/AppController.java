package com.notflix.springVideo.controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    //inietto il valore della proprietà "app.nome" dal file application.properties, se la proprietà non è definita uso "SpringVideo" come valore di default
    @Value("${app.nome:SpringVideo}")
    private String appNome;

    //il metodo che gestisce la richiesta GET per la home page, risponde alle richieste GET all'URL "/", 
    //restituisce il nome della vista (index.html) da visualizzare
    @GetMapping("/")
    public String home(Model model) { 
        //aggiungo al modello l'attributo "appNome" con il suo valore, 
        //Model è un contenitore di dati, passato alla vista (index.html) affiché possa verne accesso; è come una mappa di coppie chiave-valore, ed è un oggetto fornito da Spring MVC
       
        model.addAttribute("appNome", appNome); //la chiave è "appNome" e il valore è il nome dell'app, nel file index.html posso usare questa chiave per visualizzare il nome dell'app,sarà visualizzato dove nel file index.html c'è scritto ${scuolaNome}
        return "index.html";
    }

    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
    
}
