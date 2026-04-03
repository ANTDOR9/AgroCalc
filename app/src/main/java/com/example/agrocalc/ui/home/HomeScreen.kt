package com.example.agrocalc.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrocalc.data.Producto
import com.example.agrocalc.data.Sesion
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: AgroCalcViewModel) {
    val productos by viewModel.productos.observeAsState(emptyList())
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var showNuevaSesionDialog by remember { mutableStateOf(false) }
    var showSesionesDialog by remember { mutableStateOf(false) }

    val sesionId by viewModel.sesionActualId.observeAsState()
    LaunchedEffect(sesionId) {
        if (sesionId != null && sesionId != 0) {
            val nombre = productoSeleccionado?.nombre ?: "Producto"
            navController.navigate("session/$sesionId/$nombre")
            viewModel.resetSesionActual()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AgroCalc", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate("history") }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Historial")
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No hay productos configurados", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Ve a Ajustes para agregar productos",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Text(
                    "Selecciona un producto",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            onClick = {
                                productoSeleccionado = producto
                                showSesionesDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog: sesiones existentes o nueva
    productoSeleccionado?.let { producto ->
        if (showSesionesDialog) {
            val sesiones by viewModel.getSesionesByProducto(producto.id).observeAsState(emptyList())
            SesionesDialog(
                producto = producto,
                sesiones = sesiones,
                onDismiss = { showSesionesDialog = false },
                onNuevaSesion = {
                    showSesionesDialog = false
                    viewModel.iniciarSesion(producto.id)
                },
                onAbrirSesion = { sesion ->
                    showSesionesDialog = false
                    navController.navigate("session/${sesion.id}/${producto.nombre}")
                }
            )
        }
    }
}

@Composable
fun SesionesDialog(
    producto: Producto,
    sesiones: List<Sesion>,
    onDismiss: () -> Unit,
    onNuevaSesion: () -> Unit,
    onAbrirSesion: (Sesion) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(producto.nombre) },
        text = {
            Column {
                Button(
                    onClick = onNuevaSesion,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nueva sesión")
                }
                if (sesiones.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Sesiones anteriores:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(sesiones) { sesion ->
                            Card(
                                onClick = { onAbrirSesion(sesion) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("Sesión #${sesion.id}", fontWeight = FontWeight.Bold)
                                    Text(sesion.fecha, fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Unidad: ${producto.unidad} · Humedad ref: ${producto.humedadReferencia}%",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
}