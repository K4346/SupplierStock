package com.example.supplierstock.ui.screens.stock_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.supplierstock.data.repositories.StockRepositoryImpl
import com.example.supplierstock.domain.repositories.StockRepository

class StockListViewModel(app: Application) : AndroidViewModel(app) {

    private val stockRepository: StockRepository = StockRepositoryImpl()
    val products = stockRepository.getAllProductsFromBdAsFlow(app.applicationContext)

}