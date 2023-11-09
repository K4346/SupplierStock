package com.example.supplierstock.ui.product_info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supplierstock.data.ProductDatabase
import com.example.supplierstock.data.entities.ProductEntity
import com.example.supplierstock.domain.ValidatorUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductInfoViewModel(application: Application) : AndroidViewModel(application) {
    //    todo потом придется убрать/изменить с добавлением рум, сейчас не оч выглядит
    var productId: Int? = null

    private val _uiState = MutableStateFlow(ProductInfoUi())
    val uiState: StateFlow<ProductInfoUi> = _uiState.asStateFlow()

    val _errorSF: MutableSharedFlow<String?> = MutableSharedFlow()
    val errorSF: SharedFlow<String?> = _errorSF.asSharedFlow()
    var showError = false

    //    todo remove, provide db by di
    private val _products: MutableList<ProductEntity> = ProductDatabase.list

    private val _navigateToProductList = MutableStateFlow(false)
    val navigateToProductList: Flow<Boolean>
        get() = _navigateToProductList

    private val validatorUseCase = ValidatorUseCase()
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

    private fun getId() = _products.maxOf { it.id } + 1

    //    todo делать запрос в бд в ассинхроне, получить ответ и в любом случае вернуться на прошлый экран

    fun addProductAndReturnBack(newProduct: ProductEntity) {
        _products.add(newProduct)
        _navigateToProductList.value = true
    }

    fun editProductAndReturnBack(newProduct: ProductEntity) {
        _products[productId!!] = newProduct
        _navigateToProductList.value = true
    }

    fun initUiState(productId_: Int) {
        productId = productId_
        val product = _products.first { it.id == productId }
        product.let {
            _uiState.value = ProductInfoUi(
                name = it.name,
                price = it.price.toString(),
                quantity = it.quantity.toString(),
                supplier = it.supplier,
                email = it.email,
                phone = it.phone
            )
        }

    }

    private fun makeProduct(currentProductId: Int?): ProductEntity {
        return ProductEntity(
            id = currentProductId ?: getId(),
            name = uiState.value.name,
            price = uiState.value.price.toDoubleOrNull() ?: 0.0,
            quantity = uiState.value.quantity.toIntOrNull() ?: 0,
            supplier = uiState.value.supplier,
            email = uiState.value.email,
            phone = uiState.value.phone
        )
    }

    fun processProduct() {
        val productEntity = makeProduct(productId)
        if (productId == null) {
            addProductAndReturnBack(productEntity)
        } else {
            editProductAndReturnBack(productEntity)
        }
    }

    fun fieldsAreFine(): Boolean {
        val productNameRes = validatorUseCase.validateProductName(uiState.value.name)
        if (productNameRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(productNameRes)
            }
            return false
        }
        val priceRes = validatorUseCase.validatePrice(uiState.value.price)
        if (priceRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(priceRes)
            }
            return false
        }
        val quantityRes = validatorUseCase.validateQuantity(uiState.value.quantity)
        if (quantityRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(quantityRes)
            }
            return false
        }
        val supplierNameRes = validatorUseCase.validateSupplierName(uiState.value.supplier)
        if (supplierNameRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(supplierNameRes)
            }
            return false
        }
        val emailRes = validatorUseCase.validateEmail(uiState.value.email)
        if (emailRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(emailRes)
            }
            return false
        }
        val phoneRes = validatorUseCase.validatePhone(uiState.value.phone)
        if (phoneRes != null) {
            showError = true
            viewModelScope.launch {
                _errorSF.emit(phoneRes)
            }
            return false
        }
        return true
    }

    fun removeProduct() {
        _products.remove(_products.first { it.id==productId })
        _navigateToProductList.value = true
    }

}