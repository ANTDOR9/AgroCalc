package com.example.agrocalc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SesionDao {
    @Query("SELECT * FROM sesiones ORDER BY fecha DESC")
    fun getAllSesiones(): Flow<List<Sesion>>

    @Query("SELECT * FROM sesiones WHERE id = :id")
    suspend fun getSesionById(id: Int): Sesion?

    @Query("SELECT * FROM sesiones WHERE productoId = :productoId ORDER BY fecha DESC")
    fun getSesionesByProducto(productoId: Int): Flow<List<Sesion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(sesion: Sesion): Long

    @Update
    suspend fun actualizar(sesion: Sesion)

    @Delete
    suspend fun eliminar(sesion: Sesion)
}