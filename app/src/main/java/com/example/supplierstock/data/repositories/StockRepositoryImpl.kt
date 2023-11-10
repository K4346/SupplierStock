package com.example.supplierstock.data.repositories

import android.content.Context
import com.example.supplierstock.data.db.StockDatabase
import com.example.supplierstock.data.entities.ProductEntity
import com.example.supplierstock.domain.repositories.StockRepository
import kotlinx.coroutines.flow.Flow

class StockRepositoryImpl : StockRepository {
    override fun getAllProductsFromBd(context: Context): List<ProductEntity> {
        val dao = StockDatabase.getInstance(context).stockDao()
        return dao.getAllProducts()
    }

    override fun getAllProductsFromBdAsFlow(context: Context): Flow<List<ProductEntity>> {
        val dao = StockDatabase.getInstance(context).stockDao()
        return dao.getAllProductsAsFlow()
    }

    override fun insertProductInBd(context: Context, product: ProductEntity) {
        val dao = StockDatabase.getInstance(context).stockDao()
        return dao.insert(product)
    }

    override fun updateProductInBd(context: Context, product: ProductEntity) {
        val dao = StockDatabase.getInstance(context).stockDao()
        return dao.update(product)
    }

    override fun deleteProductInBd(context: Context, product: ProductEntity) {
        val dao = StockDatabase.getInstance(context).stockDao()
        return dao.delete(product)
    }
}