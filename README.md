# 🌾 AgroCalc

App Android para el control y cálculo de pesos en molinos agrícolas. Permite registrar el ingreso de camiones con su peso bruto, tara y porcentaje de humedad, calculando automáticamente el peso neto y peso seco en kilogramos y quintales.

---

## 📱 Capturas

> *(Próximamente)*

---

## ✨ Funcionalidades

- ➕ **Múltiples productos** — Arroz, Papa y cualquier cultivo que agregues
- 🚛 **Registro de camiones** — Peso bruto, tara y % de humedad por camión
- ⚖️ **Cálculo automático** — Peso neto, descuento por humedad y peso seco
- 📊 **Doble unidad** — Resultados en kg y quintales (qq)
- ✏️ **Editar camiones** — Modifica datos ya registrados
- 📈 **Gráfica de barras** — Visual del peso seco por camión
- 🔍 **Buscador de historial** — Filtra sesiones por producto, fecha o número
- 🌙 **Dark mode** — Interfaz oscura con partículas animadas
- 💾 **Almacenamiento local** — Base de datos SQLite con Room

---

## 🛠️ Stack tecnológico

| Tecnología | Uso |
|------------|-----|
| Kotlin | Lenguaje principal |
| Jetpack Compose | UI declarativa |
| Room (SQLite) | Base de datos local |
| Navigation Compose | Navegación entre pantallas |
| ViewModel + LiveData | Gestión de estado |
| YCharts | Gráficas de barras |
| Material 3 | Componentes de diseño |

---

## 📐 Fórmulas de cálculo

```
Peso Neto      = Peso Bruto - Peso Tara
Desc. Humedad  = Peso Neto × (% Humedad / 100)
Peso Seco      = Peso Neto - Descuento Humedad
Quintales (qq) = Peso / 46
```

---

## 🗂️ Estructura del proyecto

```
app/src/main/java/com/example/agrocalc/
├── data/                   # Base de datos (Room)
│   ├── Producto.kt         # Entidad producto
│   ├── Sesion.kt           # Entidad sesión
│   ├── Camion.kt           # Entidad camión + cálculos
│   ├── ProductoDao.kt      # Consultas productos
│   ├── SesionDao.kt        # Consultas sesiones
│   ├── CamionDao.kt        # Consultas camiones
│   ├── AgroCalcDatabase.kt # Base de datos principal
│   └── AgroCalcRepository.kt # Repositorio
├── ui/
│   ├── home/               # Pantalla principal
│   ├── session/            # Lista de camiones + gráfica
│   ├── truck/              # Formulario de camión
│   ├── history/            # Historial con buscador
│   ├── settings/           # Configuración de productos
│   ├── navigation/         # Navegación entre pantallas
│   └── ParticleBackground  # Fondo animado
├── viewmodel/
│   └── AgroCalcViewModel.kt # Lógica de negocio
├── AgroCalcApp.kt          # Aplicación principal
└── MainActivity.kt         # Actividad principal
```

---

## 🚀 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/ANTDOR9/AgroCalc.git
```

2. Abre el proyecto en **Android Studio Hedgehog** o superior

3. Sincroniza Gradle y ejecuta en un dispositivo Android (API 26+)

---

## 📋 Pendiente / Próximas funciones

- [ ] Exportar sesión a PDF
- [ ] Notificación al superar peso límite
- [ ] Modo offline con sincronización en la nube

---

## 👨‍💻 Desarrollado por

**ANTDOR9** — Aprendiendo Android con Kotlin 🚀

---

## 📄 Licencia

MIT License
