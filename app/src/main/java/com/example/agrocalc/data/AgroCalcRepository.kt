package com.example.agrocalc.data

import kotlinx.coroutines.flow.Flow

class AgroCalcRepository(private val db: AgroCalcDatabase) {

    // Productos
    val allProductos: Flow<List<Producto>> = db.productoDao().getAllProductos()
    suspend fun insertarProducto(producto: Producto) = db.productoDao().insertar(producto)
    suspend fun eliminarProducto(producto: Producto) = db.productoDao().eliminar(producto)
    suspend fun actualizarProducto(producto: Producto) = db.productoDao().actualizar(producto)

    // Sesiones
    val allSesiones: Flow<List<Sesion>> = db.sesionDao().getAllSesiones()
    suspend fun insertarSesion(sesion: Sesion): Long = db.sesionDao().insertar(sesion)
    suspend fun actualizarSesion(sesion: Sesion) = db.sesionDao().actualizar(sesion)
    suspend fun eliminarSesion(sesion: Sesion) = db.sesionDao().eliminar(sesion)
    suspend fun getSesionById(id: Int): Sesion? = db.sesionDao().getSesionById(id)
    suspend fun actualizarCamion(camion: Camion) = db.camionDao().actualizar(camion)

    suspend fun getCamionById(id: Int): Camion? = db.camionDao().getCamionById(id)

    // Camiones
    fun getCamionesBySesion(sesionId: Int): Flow<List<Camion>> =
        db.camionDao().getCamionesBySesion(sesionId)
    suspend fun insertarCamion(camion: Camion): Long = db.camionDao().insertar(camion)
    suspend fun eliminarCamion(camion: Camion) = db.camionDao().eliminar(camion)

    fun getSesionesByProducto(productoId: Int): Flow<List<Sesion>> =
        db.sesionDao().getSesionesByProducto(productoId)
}