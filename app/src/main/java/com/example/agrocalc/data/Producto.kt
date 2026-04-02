package com.example.agrocalc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val unidad: String = "kg",
    val humedadReferencia: Double = 0.0
)