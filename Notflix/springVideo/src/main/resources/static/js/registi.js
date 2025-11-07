document.addEventListener('DOMContentLoaded', () => {
    const ricercaInput = document.getElementById('ricercaRegista');
    const searchForm = document.getElementById('searchForm');
    
    if (ricercaInput && searchForm) {
        // Intercetta il tasto 'Invio' sull'input di ricerca per inviare il form
        ricercaInput.addEventListener('keyup', (event) => {
            if (event.key === 'Enter') {
                // Previene l'invio standard del browser per gestire il tutto tramite la funzione
                event.preventDefault(); 
                searchForm.submit();
            }
        });
    }
});

/**
 * Invia il form di ricerca al Controller Spring. 
 * Questa funzione è chiamata dal click sul pulsante "CERCA".
 */
function filtraRegisti() {
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.submit();
    }
}