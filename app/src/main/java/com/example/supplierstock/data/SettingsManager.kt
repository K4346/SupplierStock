package com.example.supplierstock.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.supplierstock.data.entities.ProductEntity
import com.google.gson.Gson
import java.io.File
import java.nio.charset.StandardCharsets


// todo реализовать репозиторий / лучше добавить хилт
object SettingsManager {

    private lateinit var app: Application

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var masterKey: MasterKey

    const val encrypted_product_name = "encrypted_product_data.json"

    fun initialize(context: Application) {
        app = context
        masterKey = MasterKey.Builder(app, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            SETTINGS_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    //todo убрать заглушки
    fun saveToFile(context: Context, productData: ProductEntity, uriPath: Uri) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uriPath)

        if (inputStream != null) {

            val tempFile = File(context.cacheDir, "temp_file.json")


            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }


            val encryptedFile = EncryptedFile.Builder(
                context,
                tempFile,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            tempFile.delete()

            val jsonProduct = Gson().toJson(productData)
            encryptedFile.openFileOutput().use { outputStream ->
                outputStream.write(jsonProduct.toByteArray(StandardCharsets.UTF_8))
                outputStream.flush()
            }

            encryptedFile.openFileInput().use {
                val jsonString = String(it.readBytes())

                val gson = Gson()
                val product = gson.fromJson(jsonString, ProductEntity::class.java)
                Log.i("kpoper", product.toString())
                Toast.makeText(context, "${uriPath.path} $product", Toast.LENGTH_LONG).show()
            }
            //  getProductFromEncryptedFile(context, uriPath)
        }
    }


    fun getProductFromEncryptedFile(context: Context, uriPath: Uri) {
//        val contentResolver = context.contentResolver
//        val inputStream = contentResolver.openInputStream(uriPath)
//
//        if (inputStream != null) {
//
//            val tempFile = File(context.cacheDir, "temp_file.json")
//
//            // Копируйте данные из inputStream в tempFile
//            tempFile.outputStream().use { outputStream ->
//                inputStream.copyTo(outputStream)
//            }
//
//
////            val encryptedFile = EncryptedFile.Builder(
////                context,
////                tempFile,
////                masterKey,
////                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
////            ).build()
//
//
//            val fileContent = ByteArray(32000)
//
//            //The number of bytes actually read from the file.
//            val numBytesRead: Int
//
//            //Open the file for reading, and read all the contents.
//            //Note how Kotlin's 'use' function correctly closes the resource after we've finished,
//            //regardless of whether or not an exception was thrown.
//            encryptedFile.openFileInput().use {
//                numBytesRead = it.read(fileContent)
//                Log.i("kpoper", numBytesRead.toString())
//            }
//
//            //return String(fileContent, 0, numBytesRead)
//            return
//            encryptedFile.openFileInput().use { inputStream_ ->
//
//                val jsonString = String(inputStream_.readBytes())
//
//                val gson = Gson()
//                Log.i("kpoper", gson.fromJson(jsonString, ProductEntity::class.java).toString())
//            }
//            tempFile.delete()
//        }
        Toast.makeText(context, uriPath.path, Toast.LENGTH_SHORT).show()
    }

    //    has def value -> not null - !!
    var defaultSupplierName: String
        get() = sharedPreferences.getString(DEFAULT_SUPPLIER_NAME_KEY, defaultSupplierNameValue)!!
        set(supplierName) {
            sharedPreferences.edit().putString(DEFAULT_SUPPLIER_NAME_KEY, supplierName).apply()
        }
    var defaultEmail: String
        get() = sharedPreferences.getString(DEFAULT_EMAIL_KEY, defaultEmailValue)!!
        set(email) {
            sharedPreferences.edit().putString(DEFAULT_EMAIL_KEY, email).apply()
        }
    var defaultPhone: String
        get() = sharedPreferences.getString(DEFAULT_PHONE_KEY, defaultPhoneValue)!!
        set(phone) {
            sharedPreferences.edit().putString(DEFAULT_PHONE_KEY, phone).apply()
        }
    var useDefaultValues: Boolean
        get() = sharedPreferences.getBoolean(USE_DEFAULT_VALUES_KEY, false)
        set(useDefaultValues) {
            sharedPreferences.edit().putBoolean(USE_DEFAULT_VALUES_KEY, useDefaultValues).apply()
        }
    var hideSensitiveData: Boolean
        get() = sharedPreferences.getBoolean(HIDE_SENSITIVE_DATA_KEY, false)
        set(hideSensitiveData) {
            sharedPreferences.edit().putBoolean(HIDE_SENSITIVE_DATA_KEY, hideSensitiveData)
                .apply()
        }
    var disableDataSharing: Boolean
        get() = sharedPreferences.getBoolean(DISABLE_DATA_SHARING_KEY, false)
        set(disableDataSending) {
            sharedPreferences.edit().putBoolean(DISABLE_DATA_SHARING_KEY, disableDataSending)
                .apply()
        }


    private const val defaultSupplierNameValue = ""
    private const val defaultEmailValue = ""
    private const val defaultPhoneValue = ""

    private const val SETTINGS_PREF_NAME = "supplier_stock_settings"
    private const val DEFAULT_SUPPLIER_NAME_KEY = "default_supplier_name"
    private const val DEFAULT_EMAIL_KEY = "default_email"
    private const val DEFAULT_PHONE_KEY = "default_phone"
    private const val USE_DEFAULT_VALUES_KEY = "use_default_values"
    private const val HIDE_SENSITIVE_DATA_KEY = "hide_sensitive_data"
    private const val DISABLE_DATA_SHARING_KEY = "disable_data_sharing"

}