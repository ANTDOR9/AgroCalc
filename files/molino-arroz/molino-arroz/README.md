# Molino de Arroz — Sistema de Pesaje

Aplicación web para registrar y calcular el peso de arroz en el molino.
Sin dependencias de servidor — se abre directo en el navegador.

## Estructura del proyecto

```
molino-arroz/
├── index.html          ← Entrada principal
├── css/
│   ├── tokens.css      ← Variables de diseño (colores, tipografía, etc.)
│   ├── base.css        ← Reset y estilos base
│   └── components.css  ← Componentes UI (header, tabla, botones, etc.)
├── js/
│   ├── data.js         ← Modelo de datos y estado (STATE)
│   ├── ui.js           ← Renderer y acciones de interfaz (UI)
│   ├── export.js       ← Exportación a Excel e impresión (EXPORT)
│   ├── charts.js       ← Gráficas y estadísticas (CHARTS) — listo para Chart.js
│   └── app.js          ← Inicialización, navegación y atajos de teclado
└── assets/             ← Imágenes, íconos (vacío por ahora)
```

## Cómo usar

1. Abre `index.html` en el navegador (doble clic)
2. Haz clic en **+ Nuevo día** para crear una fecha de cosecha
3. Ingresa los datos de cada carro: bruto, tara, neto declarado y humedad
4. Presiona **Enter** o el botón **+ Agregar carro**
5. Los totales se calculan automáticamente

## Fórmulas

| Cálculo | Fórmula |
|---|---|
| Neto real | Bruto − Tara |
| Error de boleta | \|Neto real − Neto declarado\| > 0.01 |
| Peso seco | Neto real × (100 − Humedad) / 86 |
| Descuento humedad | Neto real − Peso seco |

> La base de 14% da el divisor 86 = (100 − 14)

## Atajos de teclado

- `Enter` en el campo **Humedad** → agrega el carro automáticamente

## Activar gráficas (futuro)

En `index.html`, agrega antes del cierre `</body>`:
```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.4.1/chart.umd.min.js"></script>
```
Luego en `js/charts.js`, descomenta las secciones marcadas con `[CHART.JS]`.

## Personalizar el tema

Edita `css/tokens.css` para cambiar colores, fuentes y espaciado sin tocar el resto del código.

## Exportar

- **Excel**: genera un `.xlsx` con una hoja por día + resumen general
- **Imprimir**: oculta controles y muestra reporte limpio

## Próximas funciones sugeridas

- [ ] Gráficas con Chart.js
- [ ] Persistencia local (localStorage)
- [ ] Exportar PDF
- [ ] Login / múltiples usuarios
- [ ] Backend con base de datos
