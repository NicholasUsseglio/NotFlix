
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
});
const logoutTrigger = document.getElementById('logoutTrigger');
const logoutForm = document.getElementById('logoutForm');

if (logoutTrigger && logoutForm) {
    logoutTrigger.addEventListener('click', function () {
        logoutForm.submit();
    });
}

/*--------------------SLIDER*/


document.addEventListener('DOMContentLoaded', function () {
  let currentSlide = 0;
  const slides = document.querySelectorAll('.hero-slide');

  function showSlide(index) {
    slides.forEach((slide, i) => {
      slide.classList.toggle('active', i === index);
    });
  }

  window.nextSlide = function () {
    currentSlide = (currentSlide + 1) % slides.length;
    showSlide(currentSlide);
  }

  window.prevSlide = function () {
    currentSlide = (currentSlide - 1 + slides.length) % slides.length;
    showSlide(currentSlide);
  }
});



  // Seleziona tutte le sezioni con scroll
const filmSections = document.querySelectorAll('.films-wrapper');

filmSections.forEach((section, index) => {  // <--- index ci serve per sfalzare il timer
  const scrollContainer = section.querySelector('.films-scroll');
  const btnLeft = section.querySelector('.films-arrow.left');
  const btnRight = section.querySelector('.films-arrow.right');
  const scrollAmount = 625;

  btnLeft.addEventListener('click', () => {
    scrollContainer.scrollBy({ left: -scrollAmount, behavior: 'smooth' });
  });

  btnRight.addEventListener('click', () => {
    const maxScrollLeft = scrollContainer.scrollWidth - scrollContainer.clientWidth;
    if (scrollContainer.scrollLeft >= maxScrollLeft - 10) {
      scrollContainer.scrollTo({ left: 0, behavior: 'smooth' });
    } else {
      scrollContainer.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    }
  });

  // Timer sfalsato: aggiungi un delay basato sull'indice
  setTimeout(() => {
    setInterval(() => {
      const maxScrollLeft = scrollContainer.scrollWidth - scrollContainer.clientWidth;
      if (scrollContainer.scrollLeft >= maxScrollLeft - 10) {
        scrollContainer.scrollTo({ left: 0, behavior: 'smooth' });
      } else {
        scrollContainer.scrollBy({ left: scrollAmount, behavior: 'smooth' });
      }
    }, 8000);
  }, index * 4000); // ogni sezione parte 3s dopo la precedente
});

