package com.example.supplierstock.ui.screens.product_info

data class ProductInfoUiState(
    val name: String,
    val price: String,
    val quantity: String,
    val supplier: String,
    val email: String,
    val phone: String,
    val dataSource: String
)
