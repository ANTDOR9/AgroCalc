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
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: AgroCalcViewModel) {
    val productos by viewModel.productos.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }

    // Observar sesion creada y navegar UNA sola vez
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Nueva sesión")
            }
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
                    "Selecciona un producto para iniciar",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(productos) { producto ->
                        ProductoCard(producto = producto, onClick = {
                            productoSeleccionado = producto
                            viewModel.iniciarSesion(producto.id)
                        })
                    }
                }
            }
        }
    }

    if (showDialog) {
        NuevaSesionDialog(
            productos = productos,
            onDismiss = { showDialog = false },
            onConfirm = { producto ->
                productoSeleccionado = producto
                viewModel.iniciarSesion(producto.id)
                showDialog = false
            }
        )
    }
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

@Composable
fun NuevaSesionDialog(
    productos: List<Producto>,
    onDismiss: () -> Unit,
    onConfirm: (Producto) -> Unit
) {
    var seleccionado by remember { mutableStateOf(productos.firstOrNull()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva sesión") },
        text = {
            Column {
                Text("Selecciona el producto:")
                Spacer(modifier = Modifier.height(8.dp))
                productos.forEach { producto ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = seleccionado == producto,
                            onClick = { seleccionado = producto }
                        )
                        Text(producto.nombre)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { seleccionado?.let { onConfirm(it) } }) {
                Text("Iniciar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}