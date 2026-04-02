package com.example.agrocalc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CamionDao {
    @Query("SELECT * FROM camiones WHERE sesionId = :sesionId ORDER BY timestamp ASC")
    fun getCamionesBySesion(sesionId: Int): Flow<List<Camion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(camion: Camion): Long

    @Delete
    suspend fun eliminar(camion: Camion)
}