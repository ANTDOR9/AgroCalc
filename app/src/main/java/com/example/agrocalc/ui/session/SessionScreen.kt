package com.example.agrocalc.ui.session

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrocalc.data.Camion
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: AgroCalcViewModel,
    sesionId: Int,
    productoNombre: String
) {
    val camiones by viewModel.getCamionesBySesion(sesionId).observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$productoNombre · Sesión") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("truck/$sesionId")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar camión")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Resumen totales
            if (camiones.isNotEmpty()) {
                ResumenCard(camiones = camiones)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Camiones registrados (${camiones.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (camiones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sin camiones aún", fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Toca + para agregar el primer camión",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(camiones) { camion ->
                        CamionItem(
                            camion = camion,
                            numero = camiones.indexOf(camion) + 1,
                            onEliminar = { viewModel.eliminarCamion(camion) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResumenCard(camiones: List<Camion>) {
    val totalNeto = camiones.sumOf { it.pesoNeto }
    val totalSeco = camiones.sumOf { it.pesoSeco }
    val totalNetoQq = camiones.sumOf { it.pesoNetoQq }
    val totalSecoQq = camiones.sumOf { it.pesoSecoQq }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                ResumenItem(label = "Peso Neto", kg = totalNeto, qq = totalNetoQq)
                ResumenItem(label = "Peso Seco", kg = totalSeco, qq = totalSecoQq)
            }
        }
    }
}

@Composable
fun ResumenItem(label: String, kg: Double, qq: Double) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${"%.1f".format(kg)} kg", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text("${"%.2f".format(qq)} qq", fontSize = 13.sp)
    }
}

@Composable
fun CamionItem(camion: Camion, numero: Int, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Camión #$numero · ${camion.timestamp}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Bruto: ${"%.1f".format(camion.pesoBruto)} kg  |  Tara: ${"%.1f".format(camion.pesoTara)} kg")
                Text("Humedad: ${camion.humedad}%  |  Neto: ${"%.1f".format(camion.pesoNeto)} kg")
                Text(
                    "Peso seco: ${"%.1f".format(camion.pesoSeco)} kg  (${"%.2f".format(camion.pesoSecoQq)} qq)",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}