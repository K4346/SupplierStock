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
import java.io.FileInputStream
import java.io.FileOutputStream


// todo реализовать репозиторий / лучше добавить хилт
object SettingsManager {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var masterKey: MasterKey

    const val encrypted_product_name = "encrypted_product_data.json"

    const val TEMP_FILE_NAME = "temp_file.json"

    fun lazyTempFile(context: Context): File {
        val tempFile = File(context.cacheDir, TEMP_FILE_NAME)
        if (tempFile.exists()) tempFile.delete()
        return tempFile
    }

    fun initialize(app: Application) {
        masterKey = MasterKey.Builder(app, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            app,
            SETTINGS_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    //todo убрать заглушки
    fun saveToFile(context: Context, productData: ProductEntity, uriPath: Uri) {
        val tempFile = lazyTempFile(context)
        val encryptedFile = EncryptedFile.Builder(
            context,
            tempFile,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().apply {
            val jsonProduct = Gson().toJson(productData)
            write(jsonProduct.toByteArray())
            close()
        }

        context.contentResolver.openFileDescriptor(uriPath, "w")?.use { descriptor ->
            FileOutputStream(descriptor.fileDescriptor).use { output ->
                tempFile.inputStream().use { input ->
                    input.copyTo(output)
                    input.close()
                }
                output.close()
            }
        }
        tempFile.delete()
    }


    fun getProductFromEncryptedFile(context: Context, uriPath: Uri): ProductEntity {
        val tempFile = lazyTempFile(context)

        tempFile.outputStream().use { output ->
            context.contentResolver.openFileDescriptor(uriPath, "r")?.use { descriptor ->
                FileInputStream(descriptor.fileDescriptor).use { input ->
                    input.copyTo(output)
                    input.close()
                }
            }
            output.close()
        }

        val encryptedFile = EncryptedFile.Builder(
            context,
            tempFile,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
        encryptedFile.openFileInput().use { input ->
            val jsonString = String(input.readBytes())
            val gson = Gson()
            val product = gson.fromJson(jsonString, ProductEntity::class.java)
            Log.i("kpoper", product.toString())
            Toast.makeText(context, "${uriPath.path} $product", Toast.LENGTH_LONG).show()

            tempFile.delete()
            return product
        }
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