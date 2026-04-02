package com.example.agrocalc.viewmodel

import androidx.lifecycle.*
import com.example.agrocalc.data.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AgroCalcViewModel(private val repository: AgroCalcRepository) : ViewModel() {

    val productos = repository.allProductos.asLiveData()
    val sesiones = repository.allSesiones.asLiveData()

    private val _sesionActualId = MutableLiveData<Int?>()
    val sesionActualId: LiveData<Int?> = _sesionActualId

    fun getCamionesBySesion(sesionId: Int) =
        repository.getCamionesBySesion(sesionId).asLiveData()

    fun insertarProducto(nombre: String, unidad: String, humedadRef: Double) {
        viewModelScope.launch {
            repository.insertarProducto(
                Producto(nombre = nombre, unidad = unidad, humedadReferencia = humedadRef)
            )
        }
    }

    fun iniciarSesion(productoId: Int) {
        viewModelScope.launch {
            val fecha = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            val id = repository.insertarSesion(
                Sesion(productoId = productoId, fecha = fecha)
            )
            _sesionActualId.postValue(id.toInt())
        }
    }

    fun agregarCamion(sesionId: Int, pesoBruto: Double, pesoTara: Double, humedad: Double) {
        viewModelScope.launch {
            val timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm:ss"))
            repository.insertarCamion(
                Camion(
                    sesionId = sesionId,
                    pesoBruto = pesoBruto,
                    pesoTara = pesoTara,
                    humedad = humedad,
                    timestamp = timestamp
                )
            )
        }
    }

    fun eliminarCamion(camion: Camion) {
        viewModelScope.launch { repository.eliminarCamion(camion) }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch { repository.eliminarProducto(producto) }
    }
}

class AgroCalcViewModelFactory(private val repository: AgroCalcRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgroCalcViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgroCalcViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}