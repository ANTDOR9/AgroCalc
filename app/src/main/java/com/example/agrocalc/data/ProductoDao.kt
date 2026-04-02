package com.example.agrocalc.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAllProductos(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)

    @Update
    suspend fun actualizar(producto: Producto)
}