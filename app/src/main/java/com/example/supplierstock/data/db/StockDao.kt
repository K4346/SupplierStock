package com.example.supplierstock.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.supplierstock.data.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface  StockDao {
    @Query("SELECT * FROM products")
    fun getAllProductsAsFlow(): Flow<List<ProductEntity>>
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<ProductEntity>
    @Insert
    fun insert(product: ProductEntity)

    @Update
     fun update(product: ProductEntity)

    @Delete
    fun delete(product: ProductEntity)
}