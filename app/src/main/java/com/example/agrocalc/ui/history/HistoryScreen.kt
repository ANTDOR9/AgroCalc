package com.example.agrocalc.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrocalc.data.Sesion
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, viewModel: AgroCalcViewModel) {
    val sesiones: List<Sesion> by viewModel.sesiones.observeAsState(emptyList())
    val productos by viewModel.productos.observeAsState(emptyList())
    var busqueda by remember { mutableStateOf("") }

    // Filtrar sesiones por busqueda
    val sesionesFiltradas = remember(sesiones, busqueda) {
        if (busqueda.isEmpty()) sesiones
        else sesiones.filter { sesion ->
            sesion.fecha.contains(busqueda, ignoreCase = true) ||
                    sesion.id.toString().contains(busqueda) ||
                    productos.find { it.id == sesion.productoId }
                        ?.nombre?.contains(busqueda, ignoreCase = true) == true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Barra de búsqueda
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por producto, fecha o #sesión") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contador
            Text(
                "${sesionesFiltradas.size} sesiones encontradas",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (sesionesFiltradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No se encontraron sesiones", fontSize = 16.sp)
                        if (busqueda.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { busqueda = "" }) {
                                Text("Limpiar búsqueda")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = sesionesFiltradas, key = { it.id }) { sesion ->
                        val productoNombre = productos.find {
                            it.id == sesion.productoId
                        }?.nombre ?: "Producto"
                        SesionHistorialItem(
                            sesion = sesion,
                            productoNombre = productoNombre,
                            onClick = {
                                navController.navigate("session/${sesion.id}/$productoNombre")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SesionHistorialItem(sesion: Sesion, productoNombre: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(productoNombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Sesión #${sesion.id}", fontSize = 13.sp)
                Text(sesion.fecha, fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}