/* ============================================
   MOLINO DE ARROZ — UI Renderer
   js/ui.js
   ============================================ */

'use strict';

/* ── Formateador de números ── */
function fmt(n, dec = 2) {
  return Number(n).toLocaleString('es-PE', {
    minimumFractionDigits: dec,
    maximumFractionDigits: dec
  });
}

function humedadChip(h) {
  const cls = h <= 14 ? 'hum-low' : h <= 18 ? 'hum-mid' : 'hum-high';
  return `<span class="hum-chip ${cls} num">${h}%</span>`;
}

/* ── Gran Total ── */
function renderGrandTotals() {
  const gt = STATE.grandTotals();
  setText('gt-dias',    gt.dias);
  setText('gt-carros',  gt.carros);
  setText('gt-neto',    fmt(gt.netoReal) + ' kg');
  setText('gt-desc',    fmt(gt.descuento) + ' kg');
  setText('gt-seco',    fmt(gt.seco) + ' kg');
}

/* ── Lista de días ── */
function renderDays() {
  const container = document.getElementById('days-list');
  const emptyState = document.getElementById('empty-state');

  container.innerHTML = '';

  if (STATE.days.length === 0) {
    emptyState.style.display = 'block';
    renderGrandTotals();
    return;
  }

  emptyState.style.display = 'none';

  STATE.days.forEach((day, idx) => {
    const block = buildDayBlock(day, idx + 1);
    block.classList.add('animate-up');
    block.style.animationDelay = `${idx * 0.04}s`;
    container.appendChild(block);
  });

  renderGrandTotals();
}

/* ── Construye un bloque de día ── */
function buildDayBlock(day, displayNum) {
  const tot    = STATE.dayTotals(day.id);
  const errTxt = tot.errores > 0
    ? `<span class="badge badge-error">${tot.errores} error${tot.errores > 1 ? 's' : ''}</span>`
    : '';

  const block = document.createElement('div');
  block.className = 'day-block';
  block.id = `day-${day.id}`;

  block.innerHTML = `
    <div class="day-header">
      <div class="day-header-left">
        <span class="day-index">DÍA ${displayNum}</span>
        <input class="day-date-input no-print" type="date" value="${day.date}"
          onchange="STATE.updateDayDate(${day.id}, this.value); renderDays()">
        <span class="print-only" style="font-size:12px;color:var(--c-text2)">${day.date}</span>
        ${errTxt}
      </div>
      <div class="day-header-right">
        <div class="day-stats">
          <div class="day-stat">
            <div class="ds-label">Carros</div>
            <div class="ds-value">${tot.carros}</div>
          </div>
          <div class="day-stat accent">
            <div class="ds-label">Neto real</div>
            <div class="ds-value num">${fmt(tot.netoReal)} kg</div>
          </div>
          <div class="day-stat danger">
            <div class="ds-label">Desc. hum.</div>
            <div class="ds-value num">${fmt(tot.descuento)} kg</div>
          </div>
          <div class="day-stat accent">
            <div class="ds-label">Peso seco</div>
            <div class="ds-value num">${fmt(tot.seco)} kg</div>
          </div>
        </div>
        <button class="btn btn-danger-outline btn-sm no-print"
          onclick="UI.confirmRemoveDay(${day.id})">✕ Eliminar día</button>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th>Carro</th>
            <th>Bruto (kg)</th>
            <th>Tara (kg)</th>
            <th>Neto decl. (kg)</th>
            <th>Neto real (kg)</th>
            <th>Diferencia</th>
            <th>Humedad</th>
            <th>Desc. hum. (kg)</th>
            <th>Peso seco (kg)</th>
            <th class="no-print">Acciones</th>
          </tr>
        </thead>
        <tbody id="tbody-${day.id}">
          ${buildCarrosRows(day)}
        </tbody>
      </table>
    </div>

    <div class="add-carro-area no-print">
      ${buildAddForm(day.id)}
    </div>
  `;

  return block;
}

/* ── Filas de carros ── */
function buildCarrosRows(day) {
  if (day.carros.length === 0) {
    return `<tr><td colspan="10" style="text-align:center;padding:24px;color:var(--c-text3);font-size:12px;">
      Sin carros — agrega el primero abajo
    </td></tr>`;
  }

  return day.carros.map((c, i) => buildCarroRow(day.id, c, i + 1)).join('');
}

function buildCarroRow(dayId, c, num) {
  const diffTxt = c.tieneError
    ? `<span class="text-danger num">Δ ${fmt(c.diffDecl)}</span>`
    : `<span class="text-accent">—</span>`;

  const statusBadge = c.tieneError
    ? `<span class="badge badge-error">Error boleta</span>`
    : `<span class="badge badge-ok">OK</span>`;

  return `
    <tr id="row-${c.id}">
      <td>
        <span class="carro-id">#${num}</span>
        ${statusBadge}
      </td>
      <td class="num">${fmt(c.bruto)}</td>
      <td class="num">${fmt(c.tara)}</td>
      <td class="num">${fmt(c.declarado)}</td>
      <td class="num ${c.tieneError ? 'text-danger' : ''}">${fmt(c.netoReal)}</td>
      <td>${diffTxt}</td>
      <td>${humedadChip(c.humedad)}</td>
      <td class="num text-danger">${fmt(c.descuento)}</td>
      <td class="num text-accent" style="font-weight:500">${fmt(c.seco)}</td>
      <td class="no-print">
        <div style="display:flex;gap:6px;justify-content:flex-end">
          <button class="btn btn-outline btn-xs" onclick="UI.startEdit(${dayId}, ${c.id})">Editar</button>
          <button class="btn btn-danger-outline btn-xs" onclick="UI.confirmRemoveCarro(${dayId}, ${c.id})">✕</button>
        </div>
      </td>
    </tr>`;
}

/* ── Formulario agregar carro ── */
function buildAddForm(dayId) {
  return `
    <div class="add-carro-form">
      <div class="form-field">
        <label>Bruto (kg)</label>
        <input type="number" id="f-bruto-${dayId}" placeholder="0.00" step="0.01" min="0">
      </div>
      <div class="form-field">
        <label>Tara (kg)</label>
        <input type="number" id="f-tara-${dayId}" placeholder="0.00" step="0.01" min="0">
      </div>
      <div class="form-field">
        <label>Neto decl. (kg)</label>
        <input type="number" id="f-decl-${dayId}" placeholder="0.00" step="0.01" min="0">
      </div>
      <div class="form-field">
        <label>Humedad (%)</label>
        <input type="number" id="f-hum-${dayId}" placeholder="14" step="0.1" min="0" max="100" value="14">
      </div>
      <div class="form-field" style="justify-content:flex-end">
        <label>&nbsp;</label>
        <button class="btn btn-primary btn-sm" onclick="UI.submitCarro(${dayId})">+ Agregar carro</button>
      </div>
    </div>`;
}

/* ── Modo edición inline ── */
function buildEditRow(dayId, c, num) {
  return `
    <tr id="row-${c.id}" class="is-editing">
      <td><span class="carro-id">#${num}</span></td>
      <td><input type="number" id="e-bruto-${c.id}" value="${c.bruto}" step="0.01" min="0"></td>
      <td><input type="number" id="e-tara-${c.id}"  value="${c.tara}"  step="0.01" min="0"></td>
      <td><input type="number" id="e-decl-${c.id}"  value="${c.declarado}" step="0.01" min="0"></td>
      <td colspan="4"></td>
      <td><input type="number" id="e-hum-${c.id}"   value="${c.humedad}" step="0.1" min="0" max="100"></td>
      <td class="no-print">
        <div style="display:flex;gap:6px;justify-content:flex-end">
          <button class="btn btn-primary btn-xs" onclick="UI.saveEdit(${dayId}, ${c.id})">Guardar</button>
          <button class="btn btn-ghost btn-xs" onclick="refreshTbody(${dayId})">Cancelar</button>
        </div>
      </td>
    </tr>`;
}

/* ── Helpers DOM ── */
function setText(id, val) {
  const el = document.getElementById(id);
  if (el) el.textContent = val;
}

function refreshTbody(dayId) {
  const day = STATE.getDay(dayId);
  if (!day) return;
  const tbody = document.getElementById(`tbody-${dayId}`);
  if (tbody) tbody.innerHTML = buildCarrosRows(day);
}

function refreshDayHeader(dayId) {
  const day = STATE.getDay(dayId);
  if (!day) return;
  const block = document.getElementById(`day-${dayId}`);
  if (!block) return;
  const idx = STATE.days.indexOf(day) + 1;
  const newBlock = buildDayBlock(day, idx);
  block.replaceWith(newBlock);
}

/* ── Objeto UI (acciones) ── */
const UI = {
  submitCarro(dayId) {
    const bruto     = parseFloat(document.getElementById(`f-bruto-${dayId}`)?.value) || 0;
    const tara      = parseFloat(document.getElementById(`f-tara-${dayId}`)?.value)  || 0;
    const declarado = parseFloat(document.getElementById(`f-decl-${dayId}`)?.value)  || 0;
    const humedad   = parseFloat(document.getElementById(`f-hum-${dayId}`)?.value)   || 0;

    if (bruto <= 0)   { toast('Ingresa el peso bruto', 'err'); return; }
    if (tara < 0)     { toast('La tara no puede ser negativa', 'err'); return; }
    if (bruto < tara) { toast('El bruto no puede ser menor que la tara', 'err'); return; }
    if (humedad < 0 || humedad > 100) { toast('Humedad debe ser 0–100%', 'err'); return; }

    STATE.addCarro(dayId, { bruto, tara, declarado, humedad });

    ['f-bruto','f-tara','f-decl'].forEach(p => {
      const el = document.getElementById(`${p}-${dayId}`);
      if (el) el.value = '';
    });
    const humEl = document.getElementById(`f-hum-${dayId}`);
    if (humEl) humEl.value = '14';

    refreshTbody(dayId);
    refreshDayHeaderStats(dayId);
    renderGrandTotals();

    const firstInput = document.getElementById(`f-bruto-${dayId}`);
    if (firstInput) firstInput.focus();

    toast('Carro agregado', 'ok');
  },

  startEdit(dayId, carroId) {
    const day   = STATE.getDay(dayId);
    const carro = day?.carros.find(c => c.id === carroId);
    if (!carro) return;
    const num  = day.carros.indexOf(carro) + 1;
    const row  = document.getElementById(`row-${carroId}`);
    if (row) row.outerHTML = buildEditRow(dayId, carro, num);
  },

  saveEdit(dayId, carroId) {
    const bruto     = parseFloat(document.getElementById(`e-bruto-${carroId}`)?.value) || 0;
    const tara      = parseFloat(document.getElementById(`e-tara-${carroId}`)?.value)  || 0;
    const declarado = parseFloat(document.getElementById(`e-decl-${carroId}`)?.value)  || 0;
    const humedad   = parseFloat(document.getElementById(`e-hum-${carroId}`)?.value)   || 0;

    STATE.updateCarro(dayId, carroId, { bruto, tara, declarado, humedad });
    refreshTbody(dayId);
    refreshDayHeaderStats(dayId);
    renderGrandTotals();
    toast('Cambios guardados', 'ok');
  },

  confirmRemoveCarro(dayId, carroId) {
    if (!confirm('¿Eliminar este carro?')) return;
    STATE.removeCarro(dayId, carroId);
    refreshTbody(dayId);
    refreshDayHeaderStats(dayId);
    renderGrandTotals();
  },

  confirmRemoveDay(dayId) {
    const day = STATE.getDay(dayId);
    const n   = day?.carros.length || 0;
    const msg = n > 0
      ? `¿Eliminar este día y sus ${n} carro(s)?`
      : '¿Eliminar este día?';
    if (!confirm(msg)) return;
    STATE.removeDay(dayId);
    renderDays();
  }
};

/* ── Actualiza solo los stats del header de un día (sin re-render completo) ── */
function refreshDayHeaderStats(dayId) {
  refreshDayHeader(dayId);
}

/* ── Toast ── */
function toast(msg, type = 'ok') {
  const existing = document.getElementById('app-toast');
  if (existing) existing.remove();

  const el = document.createElement('div');
  el.id = 'app-toast';
  el.className = `toast toast-${type}`;

  const icon = type === 'ok' ? '✓' : type === 'err' ? '✕' : '!';
  el.innerHTML = `<span style="color:var(--c-${type === 'ok' ? 'ok' : type === 'err' ? 'danger' : 'warn'})">${icon}</span> ${msg}`;
  document.body.appendChild(el);

  setTimeout(() => el.remove(), 2800);
}
