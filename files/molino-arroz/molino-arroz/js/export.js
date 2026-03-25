/* ============================================
   MOLINO DE ARROZ — Exportación
   js/export.js
   Requiere: SheetJS (xlsx) cargado en el HTML
   ============================================ */

'use strict';

const EXPORT = {

  /* ── Excel ── */
  toExcel() {
    if (STATE.days.length === 0) {
      toast('No hay datos para exportar', 'warn');
      return;
    }

    if (typeof XLSX === 'undefined') {
      toast('Librería Excel no disponible', 'err');
      return;
    }

    const wb = XLSX.utils.book_new();

    /* Una hoja por día */
    STATE.days.forEach((day, idx) => {
      const rows = [
        /* Encabezado */
        ['#', 'Bruto (kg)', 'Tara (kg)', 'Neto Decl. (kg)', 'Neto Real (kg)',
         'Diferencia (kg)', 'Error boleta', 'Humedad (%)', 'Desc. Hum. (kg)', 'Peso Seco (kg)']
      ];

      day.carros.forEach((c, i) => {
        rows.push([
          i + 1,
          c.bruto,
          c.tara,
          c.declarado,
          c.netoReal,
          c.diffDecl,
          c.tieneError ? 'SÍ' : 'No',
          c.humedad,
          c.descuento,
          c.seco
        ]);
      });

      /* Fila de totales del día */
      const tot = STATE.dayTotals(day.id);
      rows.push([]);
      rows.push([
        'TOTAL DÍA', '', '', '',
        tot.netoReal, '', '',
        '', tot.descuento, tot.seco
      ]);

      const ws = XLSX.utils.aoa_to_sheet(rows);

      /* Ancho de columnas */
      ws['!cols'] = [
        {wch:5},{wch:12},{wch:12},{wch:14},{wch:14},
        {wch:14},{wch:12},{wch:10},{wch:14},{wch:14}
      ];

      const sheetName = `Día ${idx + 1} - ${day.date}`;
      XLSX.utils.book_append_sheet(wb, ws, sheetName);
    });

    /* Hoja de resumen general */
    const resumen = [
      ['Día', 'Fecha', '# Carros', 'Neto Real (kg)', 'Desc. Hum. (kg)', 'Peso Seco (kg)', 'Errores boleta']
    ];

    STATE.days.forEach((day, idx) => {
      const tot = STATE.dayTotals(day.id);
      resumen.push([idx + 1, day.date, tot.carros, tot.netoReal, tot.descuento, tot.seco, tot.errores]);
    });

    const gt = STATE.grandTotals();
    resumen.push([]);
    resumen.push(['GRAN TOTAL', '', gt.carros, gt.netoReal, gt.descuento, gt.seco, gt.errores]);

    const wsRes = XLSX.utils.aoa_to_sheet(resumen);
    wsRes['!cols'] = [{wch:6},{wch:12},{wch:10},{wch:14},{wch:14},{wch:14},{wch:14}];
    XLSX.utils.book_append_sheet(wb, wsRes, 'Resumen General');

    /* Guardar */
    const fecha = new Date().toISOString().split('T')[0];
    XLSX.writeFile(wb, `cosecha_arroz_${fecha}.xlsx`);
    toast('Excel exportado correctamente', 'ok');
  },

  /* ── Imprimir ── */
  print() {
    window.print();
  }
};
