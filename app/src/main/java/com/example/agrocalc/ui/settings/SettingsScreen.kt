package com.example.agrocalc.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: AgroCalcViewModel) {
    val productos by viewModel.productos.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes · Productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar producto")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Productos configurados",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (productos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No hay productos aún", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Toca + para agregar (Arroz, Papa, etc.)",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(productos) { producto ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(producto.nombre, fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp)
                                    Text(
                                        "Unidad: ${producto.unidad} · Humedad ref: ${producto.humedadReferencia}%",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { viewModel.eliminarProducto(producto) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AgregarProductoDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nombre, unidad, humedad ->
                viewModel.insertarProducto(nombre, unidad, humedad)
                showDialog = false
            }
        )
    }
}

@Composable
fun AgregarProductoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Double) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var unidad by remember { mutableStateOf("kg") }
    var humedad by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre (ej: Arroz, Papa)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Unidad: ", modifier = Modifier.padding(end = 8.dp))
                    FilterChip(
                        selected = unidad == "kg",
                        onClick = { unidad = "kg" },
                        label = { Text("kg") },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    FilterChip(
                        selected = unidad == "qq",
                        onClick = { unidad = "qq" },
                        label = { Text("qq") }
                    )
                }
                OutlinedTextField(
                    value = humedad,
                    onValueChange = { humedad = it },
                    label = { Text("% Humedad referencia") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (nombre.isNotEmpty()) {
                        onConfirm(nombre, unidad, humedad.toDoubleOrNull() ?: 0.0)
                    }
                }
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}