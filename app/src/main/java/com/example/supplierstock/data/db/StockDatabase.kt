package com.example.supplierstock.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.supplierstock.data.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        private var db: StockDatabase? = null
        private const val DB_NAME = "stock.db"
        private val Lock = Any()
        fun getInstance(context: Context): StockDatabase {
            synchronized(Lock) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(context, StockDatabase::class.java, DB_NAME).build()
                db = instance
                return instance
            }
        }
    }
}