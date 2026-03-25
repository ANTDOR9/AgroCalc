/* ============================================
   MOLINO DE ARROZ — Gráficas & Estadísticas
   js/charts.js

   ESTADO: Módulo preparado para integración futura.
   Para activar, descomenta las secciones marcadas
   con [CHART.JS] y agrega en index.html:
   <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js">

   Estructura de datos disponible vía STATE.chartsData():
   [{
     date: '2024-01-15',
     carros: 8,
     netoReal: 12400,
     seco: 11200,
     descuento: 1200,
     humedadPromedio: 17.2,
     errores: 1
   }, ...]
   ============================================ */

'use strict';

const CHARTS = {

  /* ── Render principal de la vista gráficas ── */
  render() {
    const container = document.getElementById('charts-container');
    if (!container) return;

    const data = STATE.chartsData();

    if (data.length === 0) {
      container.innerHTML = `
        <div class="charts-panel">
          <div class="coming-soon">Sin datos para graficar</div>
          <div class="chart-placeholder">Agrega días y carros en la pestaña Registro</div>
        </div>`;
      return;
    }

    /* Tarjetas de estadísticas resumen */
    container.innerHTML = `
      <div class="charts-panel">
        <div class="coming-soon">Gráficas — próximamente</div>
        <p style="font-size:12px;color:var(--c-text2);margin-bottom:16px">
          Módulo preparado · Conecta Chart.js para activar las gráficas
        </p>

        <div style="display:grid;grid-template-columns:repeat(3,1fr);gap:12px;margin-bottom:20px">
          ${this._statCard('Humedad promedio', this._avgHumedad(data).toFixed(1) + '%', 'warn')}
          ${this._statCard('Eficiencia seco/neto', this._eficiencia(data).toFixed(1) + '%', 'accent')}
          ${this._statCard('Carros por día (prom.)', this._avgCarros(data).toFixed(1), '')}
        </div>

        <div class="chart-placeholder" id="chart-peso-diario">
          Gráfica: Peso neto vs Peso seco por día
        </div>
        <div class="chart-placeholder" style="margin-top:12px" id="chart-humedad">
          Gráfica: Humedad promedio por día
        </div>
      </div>`;

    /* [CHART.JS] Descomentar cuando Chart.js esté disponible:
    this._renderPesoChart(data);
    this._renderHumedadChart(data);
    */
  },

  /* ── Helpers estadísticos ── */

  _avgHumedad(data) {
    if (!data.length) return 0;
    const sum = data.reduce((s, d) => s + d.humedadPromedio, 0);
    return sum / data.length;
  },

  _eficiencia(data) {
    const neto = data.reduce((s, d) => s + d.netoReal, 0);
    const seco = data.reduce((s, d) => s + d.seco, 0);
    return neto > 0 ? (seco / neto) * 100 : 0;
  },

  _avgCarros(data) {
    if (!data.length) return 0;
    return data.reduce((s, d) => s + d.carros, 0) / data.length;
  },

  _statCard(label, value, type) {
    const color = type === 'accent' ? 'var(--c-accent)'
                : type === 'warn'   ? 'var(--c-warn)'
                : 'var(--c-text)';
    return `
      <div style="background:var(--c-surface2);border:1px solid var(--c-border);
                  border-radius:var(--radius-md);padding:16px 18px">
        <div style="font-size:9px;letter-spacing:0.12em;text-transform:uppercase;
                    color:var(--c-text3);margin-bottom:8px">${label}</div>
        <div style="font-family:var(--font-display);font-size:26px;
                    color:${color};line-height:1">${value}</div>
      </div>`;
  },

  /* [CHART.JS] Implementaciones futuras:

  _renderPesoChart(data) {
    const ctx = document.getElementById('chart-peso-diario');
    ctx.style.height = '220px';
    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map(d => d.date),
        datasets: [
          { label: 'Neto Real', data: data.map(d => d.netoReal),
            backgroundColor: 'rgba(168,208,72,0.3)', borderColor: '#a8d048', borderWidth: 1 },
          { label: 'Peso Seco', data: data.map(d => d.seco),
            backgroundColor: 'rgba(76,175,125,0.3)', borderColor: '#4caf7d', borderWidth: 1 }
        ]
      },
      options: { responsive: true, plugins: { legend: { position: 'top' } } }
    });
  },

  _renderHumedadChart(data) {
    const ctx = document.getElementById('chart-humedad');
    ctx.style.height = '180px';
    new Chart(ctx, {
      type: 'line',
      data: {
        labels: data.map(d => d.date),
        datasets: [{
          label: 'Humedad promedio (%)',
          data: data.map(d => d.humedadPromedio),
          borderColor: '#d4a017',
          backgroundColor: 'rgba(212,160,23,0.1)',
          tension: 0.3, fill: true
        }]
      },
      options: { responsive: true }
    });
  }
  */
};
