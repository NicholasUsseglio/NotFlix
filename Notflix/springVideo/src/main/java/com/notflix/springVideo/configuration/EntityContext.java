package com.notflix.springVideo.configuration;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import com.notflix.springVideo.entities.Attore;
import com.notflix.springVideo.entities.Film;
import com.notflix.springVideo.entities.Regista;
import com.notflix.springVideo.entities.SerieTV;
import com.notflix.springVideo.entities.Utente;



/*
 * questa classe (Context) andrà a contenere la definizione dei BEAN delle entità che ci serviranno
 * quindi nel nostro caso i BEAN di studente, classe
 * 
 * Per indicare a Spring che questa è una classe di configurazione delle entità uso l'annotazione @Configuration
 */

@Configuration
public class EntityContext {

    // qui definirò i BEAN di cui ho bisogno

    @Bean   // specifico che questo metodo restituirà un bean di tipo Classe
    @Scope("prototype")
    /*
     *  con @Scope vado a specificare l'ambito del Bean
     *  qui è utilizzzato l'amito prototype: ogni volta che viene richiamato il bean, verrà creata una nuova istanza
     *  E' in opposizione con il Singleton (che è lo scope di default)
     */
    public Utente utente(Map<String,String> map){
        Utente u = new Utente();
        u.fromMap(map);
        return u;
    }


    @Bean
    @Scope("prototype")
    @Primary
    // Primary mi dice che questo bean è quello predefinito da utilizzare nel caso ci fosse piu candidati per l'iniezione della dipendenza,
    // ovvero nel caso ci fossero piu bean dello stesso tipo
    //  Primary -> contrassegna il bean scelto per l'iniezione automatica
    public Regista regista(Map<String,String> map) {
        Regista r = new Regista();
        r.fromMap(map);
        return r;
    }



    @Bean
    @Scope("prototype")
    public Attore attore(Map<String,String> map){
        Attore a = new Attore();
        a.fromMap(map);
        return a;
    }

    @Bean
    @Scope("prototype")
    public Film film(Map<String, String> map){
        Film f = new Film();
        f.fromMap(map);
        if (map.containsKey("id_regista")) {
            f.setRegista(new Regista());
            f.getRegista().setId(Long.parseLong(map.get("id_regista")));
            }
        return f;
    }

    public SerieTV serieTV(Map<String, String> map){
        SerieTV s = new SerieTV();
        s.fromMap(map);
        if(map.containsKey("id_regista")){
            s.setRegista(new Regista());
            s.getRegista().setId(Long.parseLong(map.get("id_regista")));
        }
        return s;
    }
    



    
}
