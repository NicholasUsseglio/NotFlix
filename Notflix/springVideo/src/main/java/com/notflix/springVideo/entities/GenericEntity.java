package com.notflix.springVideo.entities;
import lombok.Data;

/*
 *  LOMBOK -> libreria Java che ci aiuta riducendo il codice boilerplate (ripetitivo) attraverso l'uso di Annotazioni
 * ci fornisce strumenti per aggiungere ad esempio metodi come costruttori, getters,setters, toString, equals, hashcode
 * quindi non siamo piu obbligati ad inserirli manualmente
 * 
 * principali annotazioni di Lonbok (possiamo vederle come interfacce speciali):
 *  @Getter e @Setter  -> mi generano automaticamente getters e setters
 *  @ToString -> genera implementazione automatica toString()
 *  @EqualsAndHashcode -> genera implementazione automatica equals() e hashcode()
 *  @NoArgsConstructor, @AllArgsConstructor, @RequiredArgConstructor -> genera implementazione automatica costruttori vuoti, con tutte le proprieta, con solo le proprieta obbligatorie
 *    in particolare RequiredArg significa che prende in considerazione la proprieta final + quelle annotate con @NonNull 
 * @Data -> unica annotazione che combina tutte le precedenti (@Getter e @Setter, @ToString, @EqualsAndHashcode, @RequiredArgConstructor )
 * 
 *  @Builder - implementa il pattern crerazionale builder per creare oggetti in modo fluido (vediamo piu avanti che cosa sia)
 */

@Data
public abstract class GenericEntity implements IMappable{


    // per il momento il costruttore che ci prepara lombok è fatto così:
    // public GenericEntity() { }
    // per il resto del codice genrato vedi il file .class dentro il package target

    /*
     * si può personalizzare il modo in cui viene generato codice
     * ad esempio voglio rendere privato il setter di id
     * 
     * @Setter(AccessLevel.PRIVATE)
       private Long id;
     * 
     * 
     */
}
