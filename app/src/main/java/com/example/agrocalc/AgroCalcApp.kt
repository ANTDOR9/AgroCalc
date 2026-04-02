package com.example.agrocalc

import android.app.Application
import com.example.agrocalc.data.AgroCalcDatabase
import com.example.agrocalc.data.AgroCalcRepository

class AgroCalcApp : Application() {
    val database by lazy { AgroCalcDatabase.getDatabase(this) }
    val repository by lazy { AgroCalcRepository(database) }
}