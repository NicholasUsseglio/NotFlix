document.addEventListener('DOMContentLoaded', function () {
    // Catalogo
    const catalogoBtn = document.getElementById('catalogoBtn');
    const catalogoMenu = document.getElementById('catalogoMenu');

    if (catalogoBtn && catalogoMenu) {
        catalogoBtn.addEventListener('click', function (event) {
            event.stopPropagation();
            const isVisible = catalogoMenu.style.display === 'block';
            catalogoMenu.style.display = isVisible ? 'none' : 'block';
        });

        document.addEventListener('click', function (event) {
            if (!catalogoBtn.contains(event.target)) {
                catalogoMenu.style.display = 'none';
            }
        });
    }

    // Utente
    const utenteBtn = document.getElementById('utenteBtn');
    const utenteMenu = document.getElementById('utenteMenu');

    if (utenteBtn && utenteMenu) {
        utenteBtn.addEventListener('click', function (event) {
            event.stopPropagation();
            const isVisible = utenteMenu.style.display === 'block';
            utenteMenu.style.display = isVisible ? 'none' : 'block';
        });

        document.addEventListener('click', function (event) {
            if (!utenteBtn.contains(event.target)) {
                utenteMenu.style.display = 'none';
            }
        });
    }

    // --- Review form: AJAX submit + inline edit ---
    const reviewForm = document.getElementById('review-form');
    const reviewWrapper = document.getElementById('review-form-wrapper');
    const myReviewContainer = document.getElementById('my-review-container');
    const reviewsList = document.getElementById('reviews-list');

    function escapeHtml(s) {
        if (!s) return '';
        return s.replace(/&/g, '&amp;')
                .replace(/</g, '&lt;')
                .replace(/>/g, '&gt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&#039;');
    }

    if (reviewForm) {
        reviewForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const formData = new FormData(reviewForm);
            const action = reviewForm.getAttribute('action') || reviewForm.action;

            try {
                const resp = await fetch(action, {
                    method: 'POST',
                    body: formData,
                    headers: { 'X-Requested-With': 'XMLHttpRequest' }
                });

                if (!resp.ok) throw new Error('Network response was not ok');

                // Prefer server JSON response if present, otherwise use sent values
                let data = null;
                const ct = resp.headers.get('content-type') || '';
                if (ct.includes('application/json')) {
                    data = await resp.json();
                }

                const voto = formData.get('voto') || (data && data.voto) || '—';
                const recensione = formData.get('recensione') || (data && data.recensione) || '';

                // After a successful POST, reload the page so server-side rendering shows the authoritative state.
                // If reload fails for any reason, fall back to client-side insertion below.
                try {
                    location.reload();
                    return;
                } catch (err) {
                    // non-fatal: continue with client-side insertion as fallback
                }

                // Hide the form wrapper (and form) and insert the review into the reviews list
                if (reviewWrapper) reviewWrapper.style.display = 'none';
                else reviewForm.style.display = 'none';

                const escapedRecensione = escapeHtml(recensione);
                // Build the same markup used for reviews in the template
                const reviewNode = document.createElement('div');
                reviewNode.className = 'inserted-review';
                reviewNode.setAttribute('data-my-review', 'true');
                reviewNode.style.borderTop = '1px solid rgba(255,255,255,0.03)';
                reviewNode.style.padding = '12px 0';
                reviewNode.innerHTML = '' +
                    '<div style="display:flex;justify-content:space-between;align-items:center;">' +
                        '<div style="font-weight:700;color:#fff;">Tu</div>' +
                        '<div style="color:var(--muted);">' + escapeHtml(String(voto)) + '/5</div>' +
                    '</div>' +
                    '<div style="color:var(--muted);margin-top:8px;">' + escapedRecensione + '</div>' +
                    '<div style="margin-top:8px;">' +
                        '<button class="edit-my-review nav-pill" style="padding:6px 10px;margin-top:8px;">Modifica</button>' +
                    '</div>';

                if (reviewsList) {
                    // Insert as first review under reviews-list
                    reviewsList.insertBefore(reviewNode, reviewsList.firstChild);
                } else if (myReviewContainer) {
                    myReviewContainer.appendChild(reviewNode);
                }

                // Try to remove server-rendered duplicate reviews by matching text+voto
                try {
                    const candidates = (reviewsList ? reviewsList.children : []);
                    for (let i = candidates.length - 1; i >= 0; i--) {
                        const c = candidates[i];
                        if (c === reviewNode) continue;
                        const text = (c.innerText || '').trim();
                        if (!text) continue;
                        // crude match: must contain recensione text and voto
                        if (escapedRecensione && text.includes(recensione) && text.includes(String(voto))) {
                            c.parentNode && c.parentNode.removeChild(c);
                        }
                    }
                } catch (err) {
                    // non-fatal
                }

                // Wire up the edit button on the inserted node
                const editBtn = reviewNode.querySelector('.edit-my-review');
                if (editBtn) {
                    editBtn.addEventListener('click', function () {
                        // Show the wrapper (so it's visible even if server hid it) and prefill values
                        if (reviewWrapper) reviewWrapper.style.display = '';
                        else reviewForm.style.display = '';
                        const recensioneInput = reviewForm.querySelector('textarea[name="recensione"]');
                        const votoInput = reviewForm.querySelector('input[name="voto"]');
                        if (recensioneInput) recensioneInput.value = recensione || '';
                        if (votoInput) votoInput.value = voto || '';
                        // Scroll to the form for convenience
                        (reviewWrapper || reviewForm).scrollIntoView({ behavior: 'smooth', block: 'center' });
                    });
                }

            } catch (err) {
                console.error('Review submit error', err);
                alert('Errore nell\'invio della recensione. Riprova.');
            }
        });
    }

    // Delegate clicks for server-rendered Edit buttons (shown next to user's server-rendered review)
    document.addEventListener('click', function (e) {
        const btn = e.target.closest && e.target.closest('.edit-server-review');
        if (!btn) return;
        e.preventDefault();
        if (!reviewForm) return;
    const recensioneVal = btn.getAttribute('data-recensione') || '';
    const votoVal = btn.getAttribute('data-voto') || '';
    const recensioneInput = reviewForm.querySelector('textarea[name="recensione"]');
    const votoInput = reviewForm.querySelector('input[name="voto"]');
    if (recensioneInput) recensioneInput.value = recensioneVal || '';
    if (votoInput) votoInput.value = votoVal || '';
    if (reviewWrapper) reviewWrapper.style.display = '';
    else reviewForm.style.display = '';
    (reviewWrapper || reviewForm).scrollIntoView({ behavior: 'smooth', block: 'center' });
    });
});
const logoutTrigger = document.getElementById('logoutTrigger');
const logoutForm = document.getElementById('logoutForm');

if (logoutTrigger && logoutForm) {
    logoutTrigger.addEventListener('click', function () {
        logoutForm.submit();
    });
}
