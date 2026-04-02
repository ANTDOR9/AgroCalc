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
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TruckEntryScreen(
    navController: NavController,
    viewModel: AgroCalcViewModel,
    sesionId: Int
) {
    var pesoBruto by remember { mutableStateOf("") }
    var pesoTara by remember { mutableStateOf("") }
    var humedad by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    // Calculos en tiempo real
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
                title = { Text("Nuevo Camión") },
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
            Text("Ingresa los datos del camión", fontSize = 14.sp,
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

            // Preview en tiempo real
            if (neto > 0) {
                Spacer(modifier = Modifier.height(4.dp))
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
                        Divider()
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
                            viewModel.agregarCamion(sesionId, bruto, tara, hum)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Camión", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ResultRow(label: String, kg: Double, qq: Double, bold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(
            "${"%.1f".format(kg)} kg  |  ${"%.2f".format(qq)} qq",
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}