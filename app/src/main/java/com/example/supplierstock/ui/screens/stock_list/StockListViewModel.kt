package com.example.supplierstock.ui.screens.stock_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.supplierstock.data.ProductDatabase
import com.example.supplierstock.data.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class StockListViewModel(application: Application) : AndroidViewModel(application) {
//    todo реагировать на изменения бд
   // private val _products = Flow(GameUiState())

    val products: MutableList<Product>
    get() = ProductDatabase.list
}