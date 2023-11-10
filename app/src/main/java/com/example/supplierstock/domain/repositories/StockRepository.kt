package com.example.supplierstock.domain.repositories

import android.content.Context
import com.example.supplierstock.data.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getAllProductsFromBdAsFlow(context: Context): Flow<List<ProductEntity>>
    fun getAllProductsFromBd(context: Context): List<ProductEntity>

    fun insertProductInBd(context: Context, product: ProductEntity)

    fun updateProductInBd(context: Context, product: ProductEntity)

    fun deleteProductInBd(context: Context, product: ProductEntity)
}