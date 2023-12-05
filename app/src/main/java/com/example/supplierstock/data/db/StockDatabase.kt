package com.example.supplierstock.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.supplierstock.data.SettingsManager
import com.example.supplierstock.data.entities.ProductEntity
import net.sqlcipher.database.SupportFactory
import javax.crypto.KeyGenerator


@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)

abstract class StockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao

    companion object {
        private var db: StockDatabase? = null
        const val DB_NAME = "stock.db"

        private val Lock = Any()
        fun getInstance(context: Context): StockDatabase {
            synchronized(Lock) {
                var instance = db

                if (instance == null) {
                    instance = createDB(context)
                    db = instance
                }

                return instance
            }
        }

        private fun createDB(context: Context): StockDatabase {
            val passphrase = getPassphrase() ?: initializePassphrase(context)
            val factory = SupportFactory(passphrase)
            return Room.databaseBuilder(
                context.applicationContext,
                StockDatabase::class.java,
                DB_NAME
            )
                .openHelperFactory(factory)
                .fallbackToDestructiveMigration()
                .build()
        }

        private fun getPassphrase(): ByteArray? {
            val passphraseString = SettingsManager.passphraseString
            return passphraseString?.toByteArray(Charsets.ISO_8859_1)
        }

        private fun generatePassphrase(): ByteArray {
            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(256)
            return keyGenerator.generateKey().encoded
        }

        private fun initializePassphrase(context: Context): ByteArray {
            val passphrase = generatePassphrase()
            SettingsManager.passphraseString = passphrase.toString(Charsets.ISO_8859_1)

            return passphrase
        }
    }
}