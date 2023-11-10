package com.example.supplierstock.ui.screens.product_info

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.supplierstock.data.entities.ProductEntity
import com.example.supplierstock.data.repositories.StockRepositoryImpl
import com.example.supplierstock.domain.repositories.StockRepository
import com.example.supplierstock.domain.use_cases.ShareUseCase
import com.example.supplierstock.domain.use_cases.ValidatorUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductInfoViewModel(private val app: Application) : AndroidViewModel(app) {

    private var currentProduct: ProductEntity? = null
    private var productId: Int? = null

    private val _uiState = MutableStateFlow(ProductInfoUi())
    val uiState: StateFlow<ProductInfoUi> = _uiState.asStateFlow()

    private val _errorSF: MutableSharedFlow<String?> = MutableSharedFlow()
    val errorSF: SharedFlow<String?> = _errorSF.asSharedFlow()
    var showError = false

    private val stockRepository: StockRepository = StockRepositoryImpl()

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

    private fun addProductAndReturnBack(newProduct: ProductEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.insertProductInBd(app, newProduct)
        }
        _navigateToProductList.value = true
    }

    fun editProductAndReturnBack(newProduct: ProductEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.updateProductInBd(app, newProduct)
        }
        _navigateToProductList.value = true
    }

    fun initUiState(productId0: Int) {
        productId = productId0
        viewModelScope.launch(Dispatchers.IO) {
            currentProduct = stockRepository.getAllProductsFromBd(app).first { it.id == productId }
            currentProduct?.let {
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
    }

    private fun makeProduct(currentProductId: Int?): ProductEntity {
        return ProductEntity(
            id = currentProductId,
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

    fun removeTheCurrentProduct() {
        if (currentProduct == null) return
        viewModelScope.launch(Dispatchers.IO) {
            stockRepository.deleteProductInBd(app, currentProduct!!)
        }
        _navigateToProductList.value = true
    }
    fun shareData(activityContext: ComponentActivity?) {
        if (currentProduct == null) return
        val shareUseCase = ShareUseCase()
        activityContext?.startActivity(shareUseCase.makeIntent(currentProduct!!, activityContext))
    }
}