/* ============================================
   MOLINO DE ARROZ — Data Model & State
   js/data.js
   ============================================ */

'use strict';

const STATE = {
  days: [],
  _idCounter: 0,
  _carroCounter: {},

  nextId() { return ++this._idCounter; },

  nextCarroNum(dayId) {
    if (!this._carroCounter[dayId]) this._carroCounter[dayId] = 0;
    return ++this._carroCounter[dayId];
  },

  /* ── Operaciones de días ── */

  addDay(dateStr) {
    const id = this.nextId();
    this._carroCounter[id] = 0;
    const day = { id, date: dateStr || todayISO(), carros: [] };
    this.days.push(day);
    return day;
  },

  removeDay(dayId) {
    const idx = this.days.findIndex(d => d.id === dayId);
    if (idx === -1) return false;
    this.days.splice(idx, 1);
    return true;
  },

  updateDayDate(dayId, dateStr) {
    const day = this.days.find(d => d.id === dayId);
    if (day) day.date = dateStr;
  },

  getDay(dayId) { return this.days.find(d => d.id === dayId) || null; },

  /* ── Operaciones de carros ── */

  addCarro(dayId, { bruto, tara, declarado, humedad }) {
    const day = this.getDay(dayId);
    if (!day) return null;

    const id      = this.nextId();
    const num     = this.nextCarroNum(dayId);
    const netoReal = round2(bruto - tara);
    const seco     = round2(calcPesoSeco(netoReal, humedad));
    const descuento = round2(netoReal - seco);
    const diffDecl  = round2(netoReal - declarado);
    const tieneError = Math.abs(diffDecl) > 0.01;

    const carro = { id, num, bruto, tara, declarado, humedad, netoReal, seco, descuento, diffDecl, tieneError };
    day.carros.push(carro);
    return carro;
  },

  updateCarro(dayId, carroId, { bruto, tara, declarado, humedad }) {
    const day = this.getDay(dayId);
    if (!day) return null;
    const carro = day.carros.find(c => c.id === carroId);
    if (!carro) return null;

    carro.bruto      = bruto;
    carro.tara       = tara;
    carro.declarado  = declarado;
    carro.humedad    = humedad;
    carro.netoReal   = round2(bruto - tara);
    carro.seco       = round2(calcPesoSeco(carro.netoReal, humedad));
    carro.descuento  = round2(carro.netoReal - carro.seco);
    carro.diffDecl   = round2(carro.netoReal - declarado);
    carro.tieneError = Math.abs(carro.diffDecl) > 0.01;
    return carro;
  },

  removeCarro(dayId, carroId) {
    const day = this.getDay(dayId);
    if (!day) return false;
    const idx = day.carros.findIndex(c => c.id === carroId);
    if (idx === -1) return false;
    day.carros.splice(idx, 1);
    return true;
  },

  /* ── Totales ── */

  dayTotals(dayId) {
    const day = this.getDay(dayId);
    if (!day) return null;
    return calcTotals(day.carros);
  },

  grandTotals() {
    const allCarros = this.days.flatMap(d => d.carros);
    return {
      dias: this.days.length,
      ...calcTotals(allCarros)
    };
  },

  /* ── Datos para gráficas (hook futuro) ── */
  chartsData() {
    return this.days.map(d => {
      const tot = calcTotals(d.carros);
      return {
        date: d.date,
        carros: d.carros.length,
        netoReal: tot.netoReal,
        seco: tot.seco,
        descuento: tot.descuento,
        humedadPromedio: d.carros.length
          ? round2(d.carros.reduce((s, c) => s + c.humedad, 0) / d.carros.length)
          : 0,
        errores: tot.errores
      };
    });
  }
};

/* ── Helpers puros ── */

function calcPesoSeco(netoReal, humedad) {
  return netoReal * (100 - humedad) / 86;
}

function calcTotals(carros) {
  const netoReal  = carros.reduce((s, c) => s + c.netoReal, 0);
  const seco      = carros.reduce((s, c) => s + c.seco, 0);
  const descuento = carros.reduce((s, c) => s + c.descuento, 0);
  const errores   = carros.filter(c => c.tieneError).length;
  return {
    carros: carros.length,
    netoReal: round2(netoReal),
    seco: round2(seco),
    descuento: round2(descuento),
    errores
  };
}

function round2(n) { return Math.round(n * 100) / 100; }

function todayISO() { return new Date().toISOString().split('T')[0]; }
