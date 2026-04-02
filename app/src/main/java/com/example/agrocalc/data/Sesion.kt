package com.example.agrocalc.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesiones")
data class Sesion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productoId: Int,
    val fecha: String,
    val estado: String = "activa"
)
