(function(){
  function setupStarRating(containerId, inputId) {
    const container = document.getElementById(containerId);
    const input = document.getElementById(inputId);
    if (!container || !input) return;

    const back  = container.querySelector('.stars-back');
    const front = container.querySelector('.stars-front');

    if (!front.textContent.trim()) front.textContent = back.textContent || '★★★★★';

    // Fissa le dimensioni sull'esatto testo di back
    const backRect = back.getBoundingClientRect();
    container.style.width  = backRect.width  + 'px';
    container.style.height = backRect.height + 'px';

    const clamp = (v,min,max)=> Math.max(min, Math.min(max, v));
    const valueToPercent = v => clamp(v,0,5) / 5 * 100;

    function setVisual(v){
      front.style.width = valueToPercent(v) + '%';
      input.value = v;
      container.setAttribute('data-value', v);
      container.setAttribute('aria-valuenow', v);
    }

    setVisual(parseFloat(input.value) || 0);

    function eventToValue(e){
      const r = container.getBoundingClientRect();
      const clientX = (e.touches ? e.touches[0].clientX : e.clientX);
      const x = clamp(clientX - r.left, 0, r.width);
      const raw = (x / r.width) * 5;
      return Math.round(raw * 2) / 2; // step 0.5
    }

    container.addEventListener('mousemove', e=>{
      front.style.width = valueToPercent(eventToValue(e)) + '%';
    });

    container.addEventListener('mouseleave', ()=>{
      setVisual(parseFloat(input.value) || 0);
    });

    container.addEventListener('click', e=>{
      setVisual(eventToValue(e));
    });

    container.addEventListener('touchmove', e=>{
      front.style.width = valueToPercent(eventToValue(e)) + '%';
      e.preventDefault();
    }, {passive:false});

    container.addEventListener('touchend', e=>{
      setVisual(eventToValue(e));
    });

    container.addEventListener('keydown', e=>{
      const curr = parseFloat(input.value) || 0;
      if (e.key === 'ArrowRight' || e.key === 'ArrowUp')   { setVisual(clamp(curr+0.5,0,5)); e.preventDefault(); }
      if (e.key === 'ArrowLeft'  || e.key === 'ArrowDown') { setVisual(clamp(curr-0.5,0,5)); e.preventDefault(); }
      if (e.key === 'Home') { setVisual(0); e.preventDefault(); }
      if (e.key === 'End')  { setVisual(5); e.preventDefault(); }
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', ()=> setupStarRating('star-rating','voto-input'));
  } else {
    setupStarRating('star-rating','voto-input');
  }
})();
