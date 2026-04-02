package com.example.agrocalc.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
                .padding(16.dp)
        ) {
            if (sesiones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay sesiones registradas", fontSize = 16.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(items = sesiones, key = { sesion -> sesion.id }) { sesion ->
                        SesionHistorialItem(
                            sesion = sesion,
                            onClick = {
                                navController.navigate("session/${sesion.id}/Sesión")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SesionHistorialItem(sesion: Sesion, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Sesión #${sesion.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(sesion.fecha, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("Estado: ${sesion.estado}", fontSize = 12.sp)
            }
        }
    }
}