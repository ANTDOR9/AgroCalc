package com.example.agrocalc.ui.session

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    navController: NavController,
    viewModel: AgroCalcViewModel,
    sesionId: Int,
    productoNombre: String
) {
    val camiones by viewModel.getCamionesBySesion(sesionId).observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráfica · $productoNombre") },
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
            if (camiones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay camiones para graficar", fontSize = 16.sp)
                }
            } else {
                Text(
                    "Peso seco por camión (kg)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                val maxValor = camiones.maxOf { it.pesoSeco }.toFloat()

                val barras = camiones.mapIndexed { index, camion ->
                    BarData(
                        point = Point(index.toFloat(), camion.pesoSeco.toFloat()),
                        color = if (index % 2 == 0) Color(0xFF00E5FF) else Color(0xFF00BFA5),
                        label = "#${index + 1}"
                    )
                }

                val xAxis = AxisData.Builder()
                    .axisStepSize(30.dp)
                    .steps(camiones.size - 1)
                    .labelData { i -> "C${i + 1}" }
                    .axisLabelColor(Color(0xFF9AA0B4))
                    .axisLineColor(Color(0xFF9AA0B4))
                    .build()

                val yAxis = AxisData.Builder()
                    .steps(5)
                    .labelData { i -> "${"%.0f".format(i * maxValor / 5)} kg" }
                    .axisLabelColor(Color(0xFF9AA0B4))
                    .axisLineColor(Color(0xFF9AA0B4))
                    .build()

                val barChartData = BarChartData(
                    chartData = barras,
                    xAxisData = xAxis,
                    yAxisData = yAxis,
                    barStyle = BarStyle(
                        paddingBetweenBars = 8.dp,
                        barWidth = 25.dp
                    ),
                    backgroundColor = Color(0xFF121828)
                )

                BarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    barChartData = barChartData
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Detalle por camión", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(camiones.size) { index ->
                        val camion = camiones[index]
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Camión #${index + 1}", fontWeight = FontWeight.Bold)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("${"%.1f".format(camion.pesoSeco)} kg",
                                        color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
                                    Text("${"%.2f".format(camion.pesoSecoQq)} qq",
                                        fontSize = 12.sp, color = Color(0xFF9AA0B4))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}