document.addEventListener("DOMContentLoaded", () => {
    // Bottoni principali
    const btnModifica = document.getElementById("btnModificaProfilo");
    const btnPreferiti = document.getElementById("btnPreferiti");
    const btnToWatch = document.getElementById("btnToWatch");
    const btnRecensioni = document.getElementById("btnRecensioni");
    const btnAnnulla = document.getElementById("btnAnnulla");

    // Sezioni
    const contenutoProfilo = document.getElementById("contenutoProfilo");
    const contenutoPreferiti = document.getElementById("contenutoPreferiti");
    const contenutoToWatch = document.getElementById("contenutoToWatch");
    const contenutoRecensioni = document.getElementById("contenutoRecensioni");

    const valoriOriginali = {};

    // Funzione generica per mostrare una sezione e nascondere le altre
    function mostraSezione(sezione) {
        [contenutoProfilo, contenutoPreferiti, contenutoToWatch, contenutoRecensioni].forEach(s => {
            if (s) s.style.display = "none";
        });
        if (sezione) sezione.style.display = "flex";
    }

    // Salva valori profilo originali
    function salvaValoriOriginali() {
        document.querySelectorAll("#contenutoProfilo input").forEach(input => {
            valoriOriginali[input.name] = input.value;
        });
    }

    // Event listener per i bottoni principali
    if (btnModifica) {
        btnModifica.addEventListener("click", () => {
            mostraSezione(contenutoProfilo);
            salvaValoriOriginali();
        });
    }

    if (btnPreferiti) {
        btnPreferiti.addEventListener("click", () => {
            mostraSezione(contenutoPreferiti);
        });
    }

    if (btnToWatch) {
        btnToWatch.addEventListener("click", () => {
            mostraSezione(contenutoToWatch);
        });
    }

    if (btnRecensioni) {
        btnRecensioni.addEventListener("click", () => {
            mostraSezione(contenutoRecensioni);
        });
    }

    // Reset su annulla
    if (btnAnnulla) {
        btnAnnulla.addEventListener("click", () => {
            if (contenutoProfilo) contenutoProfilo.style.display = "none";
            document.querySelectorAll("#contenutoProfilo input").forEach(input => {
                input.style.display = "none";
                input.value = valoriOriginali[input.name] || "";
            });
        });
    }

    // Gestione modifica singoli campi
    document.querySelectorAll(".btn-edit").forEach(button => {
        button.addEventListener("click", () => {
            const parent = button.closest(".info-box");
            if (!parent) return;
            const input = parent.querySelector("input");
            if (input) {
                input.style.display = "block";
                input.focus();
            }
        });
    });
});

document.querySelectorAll(".recensione-box").forEach(box => {
    box.addEventListener("click", () => {
        const tipo = box.querySelector("span.tipo").textContent.trim();
        const id = box.getAttribute("data-idIntrattenimento");
        const url = tipo === "film" ? `/film/${id}` : `/serie/${id}`;
        window.location.href = url;
    });
});

//------------------AREA ADMIN-------------------------

//--------------MODALE PER MODIFICARE IL PROFILO AMMINISTRATORE
function apriModaleUtente(userId) {
    // Crea il modale dinamicamente
    const modal = document.createElement("div");
    modal.className = "modal-overlay";
    modal.innerHTML = `
        <div class="modal">
            <h2>Modifica Utente</h2>
            <form id="editUserForm">
                <input type="hidden" name="id" value="${userId}">
                <label>Nome:</label><input type="text" name="nome">
                <label>Cognome:</label><input type="text" name="cognome">
                <label>Email:</label><input type="email" name="email">
                <label>Username:</label><input type="text" name="username">
                <label>Nazionalità:</label><input type="text" name="nazionalita">
                <label>Data di nascita:</label><input type="date" name="dataNascita">
                <div class="modal-buttons">
                    <button type="submit" class="btn-salva">💾 Salva</button>
                    <button type="button" class="btn-annulla" onclick="chiudiModale()">✖ Chiudi</button>
                </div>
            </form>
        </div>
    `;
    document.body.appendChild(modal);

    // ORA il form esiste → aggiungo l’event listener
    const form = modal.querySelector("#editUserForm");
    form.addEventListener("submit", function(e){
        e.preventDefault();
        const formData = new FormData(form);
        fetch("/auth/admin/modificaUtente", {
            method: "PUT",
            body: new URLSearchParams(formData),
            headers: { "X-CSRF-TOKEN": document.querySelector('meta[name="_csrf"]').getAttribute("content") }
        })
        .then(r => {
            if (r.ok) {
                alert("Utente aggiornato con successo!");
                chiudiModale();
                window.location.reload();
            } else {
                alert("Errore durante l’aggiornamento dell’utente.");
            }
        });
    });
}

const contenutoProfilo = document.getElementById("contenutoProfilo");
const linkApriProfilo = document.getElementById("openModalBtn");

if(linkApriProfilo && contenutoProfilo){
    linkApriProfilo.addEventListener("click", (e) => {
        e.preventDefault(); // blocca la navigazione
        contenutoProfilo.style.display = "flex"; // mostra il modale
    });
}
