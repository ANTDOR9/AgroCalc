package com.example.agrocalc.ui.truck

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.agrocalc.data.Camion
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTruckScreen(
    navController: NavController,
    viewModel: AgroCalcViewModel,
    camion: Camion
) {
    var pesoBruto by remember { mutableStateOf(camion.pesoBruto.toString()) }
    var pesoTara by remember { mutableStateOf(camion.pesoTara.toString()) }
    var humedad by remember { mutableStateOf(camion.humedad.toString()) }
    var errorMsg by remember { mutableStateOf("") }

    val bruto = pesoBruto.toDoubleOrNull() ?: 0.0
    val tara = pesoTara.toDoubleOrNull() ?: 0.0
    val hum = humedad.toDoubleOrNull() ?: 0.0
    val neto = bruto - tara
    val descuento = neto * (hum / 100)
    val seco = neto - descuento
    val netoQq = neto / 46.0
    val secoQq = seco / 46.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Camión") },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Modifica los datos del camión", fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            OutlinedTextField(
                value = pesoBruto,
                onValueChange = { pesoBruto = it },
                label = { Text("Peso Bruto (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pesoTara,
                onValueChange = { pesoTara = it },
                label = { Text("Peso Tara (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = humedad,
                onValueChange = { humedad = it },
                label = { Text("% Humedad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            if (neto > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Vista previa", fontWeight = FontWeight.Bold)
                        ResultRow(label = "Peso Neto", kg = neto, qq = netoQq)
                        ResultRow(label = "Descuento humedad", kg = descuento, qq = descuento / 46.0)
                        HorizontalDivider()
                        ResultRow(label = "Peso Seco", kg = seco, qq = secoQq, bold = true)
                    }
                }
            }

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    when {
                        pesoBruto.isEmpty() -> errorMsg = "Ingresa el peso bruto"
                        pesoTara.isEmpty() -> errorMsg = "Ingresa el peso tara"
                        humedad.isEmpty() -> errorMsg = "Ingresa el % de humedad"
                        neto <= 0 -> errorMsg = "El peso neto debe ser mayor a 0"
                        else -> {
                            viewModel.editarCamion(
                                camion.copy(
                                    pesoBruto = bruto,
                                    pesoTara = tara,
                                    humedad = hum
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios", fontSize = 16.sp)
            }
        }
    }
}