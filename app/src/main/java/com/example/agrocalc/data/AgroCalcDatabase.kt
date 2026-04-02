package com.example.agrocalc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Producto::class, Sesion::class, Camion::class],
    version = 1,
    exportSchema = false
)
abstract class AgroCalcDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun sesionDao(): SesionDao
    abstract fun camionDao(): CamionDao

    companion object {
        @Volatile
        private var INSTANCE: AgroCalcDatabase? = null

        fun getDatabase(context: Context): AgroCalcDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgroCalcDatabase::class.java,
                    "agrocalc_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}