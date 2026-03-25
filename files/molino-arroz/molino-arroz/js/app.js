/* ============================================
   MOLINO DE ARROZ — App Entry Point
   js/app.js
   ============================================ */

'use strict';

/* ── Navegación de vistas ── */
function showView(view) {
  document.querySelectorAll('.view').forEach(v => v.style.display = 'none');
  document.querySelectorAll('.nav-tab').forEach(t => t.classList.remove('active'));

  const viewEl = document.getElementById(`view-${view}`);
  const tabEl  = document.getElementById(`tab-${view}`);

  if (viewEl) viewEl.style.display = 'block';
  if (tabEl)  tabEl.classList.add('active');

  if (view === 'charts') CHARTS.render();
}

/* ── Agregar nuevo día ── */
function addDay() {
  STATE.addDay();
  showView('registro');
  renderDays();
  toast('Día agregado', 'ok');
  setTimeout(() => {
    const last = document.querySelector('.day-block:last-child');
    if (last) last.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }, 100);
}

/* ── Atajos de teclado ── */
document.addEventListener('keydown', function(e) {
  /* Enter en campo humedad → enviar carro */
  if (e.key === 'Enter') {
    const el = document.activeElement;
    if (el && el.id && el.id.startsWith('f-hum-')) {
      const dayId = parseInt(el.id.replace('f-hum-', ''));
      if (!isNaN(dayId)) UI.submitCarro(dayId);
    }
    /* Enter en campo de edición → guardar */
    if (el && el.id && el.id.startsWith('e-hum-')) {
      const carroId = parseInt(el.id.replace('e-hum-', ''));
      const row = document.getElementById(`row-${carroId}`);
      if (row) {
        const btn = row.querySelector('.btn-primary');
        if (btn) btn.click();
      }
    }
  }

  /* Tab en campo bruto → avanzar dentro del form del mismo día */
});

/* ── Init ── */
document.addEventListener('DOMContentLoaded', function() {
  showView('registro');
  renderDays();
});
