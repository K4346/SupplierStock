package com.example.supplierstock.data.entities

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val supplier: String,
    val email: String,
    val phone: String
)