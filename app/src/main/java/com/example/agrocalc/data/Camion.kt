package com.example.agrocalc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "camiones")
data class Camion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sesionId: Int,
    val pesoBruto: Double,
    val pesoTara: Double,
    val humedad: Double,
    val timestamp: String
) {
    val pesoNeto: Double get() = pesoBruto - pesoTara
    val descuentoHumedad: Double get() = pesoNeto * (humedad / 100)
    val pesoSeco: Double get() = pesoNeto - descuentoHumedad
    val pesoNetoQq: Double get() = pesoNeto / 46.0
    val pesoSecoQq: Double get() = pesoSeco / 46.0
}