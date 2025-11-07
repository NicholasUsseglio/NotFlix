package com.notflix.springVideo.controllers;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.notflix.springVideo.dto.UtenteDTO;

import com.notflix.springVideo.services.UtenteService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/auth")//creare una root per i metodi di un controller, tutti i metodi partono da /auth
public class AuthController {

    @Autowired
    private UtenteService utenteService;

    //con request mapping, l'url completo diventa: http://localhost:8080/auth/login
    @GetMapping("/login") //porta alla pagina di log in 
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model,
                            HttpSession session) {

        if (error != null) {
            Exception ex = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            String msg = (ex != null) ? ex.getMessage() : "Credenziali non valide.";
            model.addAttribute("errorMessage", msg);
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Logout effettuato con successo.");
        }

        return "login"; // nome del tuo template Thymeleaf
    }

    @PostMapping("/login") //prendei dati del form di log in, quindi essendo un metodo post e non get come sopra posso mettere lo stesso nome, altrimenti dovevo cambiarlo.
    public String login(@RequestParam String username, @RequestParam String password,
                        Model model, HttpSession session, RedirectAttributes redirectAttributes) {
    
        //controllare che la password sia corretta, devo confrontarla con quella scritta nel database. 
        //select nel db per recuperare dati memorizzati, nelle repository
        UtenteDTO utente = utenteService.getByUsernameAndPassword(username, password);
        if (utente == null){
            return "login"; //inserire nel model un messaggio di errore se non trova nulla
        }else{
        session.setAttribute("role", utente.getRole());
        model.addAttribute("messaggio", "Login effettuato con successo! Benvenuto!");
        //se sono sicuro che alla colonna role esista già un valore, predefinito dall'admin, posso fare  session.setAttribute("role", utente .getrRole())
        return "login";} //se lo trova lo reindirizziamo all'homepage

    }
   // al log in viene generato un token associato all'utente, quindi quando naviga nella pagina viene passato il token e se è corretto vede la pagina in base ai suoi privilegi, se user se admin etc..
   //token inserito nella testa della richiesta http, in alternativa a SPRING SECURITY (molto più sicuro) uso http session per salvare i dati fino al logout.
    

    //gestiamo il logout
    @GetMapping("/logout")
    public String logout(HttpSession  session) {
       session.invalidate(); //devo invalidare la sessione

        return "redirect:/";//ritorno alla home
    }

    @GetMapping("/register") //apre il form di registrazione
    public String register() {
        return "register";
    }
    
    @PostMapping("/register") //effettua la registrazione
    public String registerUser(@RequestParam Map<String,Object> userData, Model model ) {
        //model da usare per inviare dei messaggi nella pagina 
        String username = (String) userData.get("username");
        //verifico la maggiore età
        String dataNascitaStr = (String) userData.get("dataNascita");
        LocalDate dataNascita = LocalDate.parse(dataNascitaStr);
        LocalDate oggi = LocalDate.now();
        Period eta = Period.between(dataNascita, oggi);
        if (eta.getYears() < 14) {
        model.addAttribute("errore", "Devi avere almeno 14 anni per registrarti");
        return "register";
    }
        if (utenteService.usernameExists(username)) {
        model.addAttribute("errore", "Username già esistente");
        return "register";
    }
        userData.put("role", "USER");
        boolean success = utenteService.save(userData);
            if (!success) {
                model.addAttribute("errore", "Errore nella registrazione");
                return "register";
            } else {
                model.addAttribute("messaggio", "Registrazione avvenuta con successo!");
                return "register"; //apre il login oppure metto redirect:/auth/login
            }
        
    }
    
    
    
}
