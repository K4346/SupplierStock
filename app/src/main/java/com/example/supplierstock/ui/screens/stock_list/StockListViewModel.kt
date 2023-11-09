package com.example.supplierstock.ui.screens.stock_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.supplierstock.data.ProductDatabase
import com.example.supplierstock.data.entities.ProductEntity

class StockListViewModel(application: Application) : AndroidViewModel(application) {
//    todo реагировать на изменения бд
    // private val _products = Flow(GameUiState())

    val products: MutableList<ProductEntity>
        get() = ProductDatabase.list
}