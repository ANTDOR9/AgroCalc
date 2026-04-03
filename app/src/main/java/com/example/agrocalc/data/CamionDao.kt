package com.example.agrocalc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CamionDao {
    @Query("SELECT * FROM camiones WHERE sesionId = :sesionId ORDER BY timestamp ASC")
    fun getCamionesBySesion(sesionId: Int): Flow<List<Camion>>


    @Query("SELECT * FROM camiones WHERE id = :id")
    suspend fun getCamionById(id: Int): Camion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(camion: Camion): Long

    @Delete
    suspend fun eliminar(camion: Camion)


    @Update
    suspend fun actualizar(camion: Camion)


}