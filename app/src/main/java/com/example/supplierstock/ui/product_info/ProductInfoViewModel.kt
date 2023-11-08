package com.example.supplierstock.ui.product_info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.supplierstock.data.ProductDatabase
import com.example.supplierstock.data.entities.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductInfoViewModel(application: Application) : AndroidViewModel(application) {
//    todo потом придется убрать/изменить с добавлением рум, сейчас не оч выглядит
    var productId: Int = -1

    private val _uiState = MutableStateFlow(ProductInfoUi())
    val uiState: StateFlow<ProductInfoUi> = _uiState.asStateFlow()

    //    todo remove, provide db by di
    private val _products: MutableList<Product> = ProductDatabase.list

    private val _navigateToProductList = MutableStateFlow(false)
    val navigateToProductList: Flow<Boolean>
        get() = _navigateToProductList

    fun onNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(name = newValue)
    }

    fun onPriceChange(newValue: String) {
        _uiState.value = _uiState.value.copy(price = newValue)
    }

    fun onQuantityChange(newValue: String) {
        _uiState.value = _uiState.value.copy(quantity = newValue)
    }

    fun onSupplierChange(newValue: String) {
        _uiState.value = _uiState.value.copy(supplier = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onPhoneChange(newValue: String) {
        _uiState.value = _uiState.value.copy(phone = newValue)
    }

    fun getId() = _products.maxOf { it.id } + 1

    //    todo делать запрос в бд в ассинхроне, получить ответ и в любом случае вернуться на прошлый экран
    fun editProductAndReturnBack(newProduct: Product) {
        _products[productId] = newProduct
        _navigateToProductList.value = true
    }

    fun initUiState(productId_: Int) {
        productId = productId_
        val product = _products[productId]
        with(product) {
            _uiState.value = ProductInfoUi(
                name = name,
                price = price.toString(),
                quantity = quantity.toString(),
                supplier = supplier,
                email = email,
                phone = phone
            )
        }

    }

}