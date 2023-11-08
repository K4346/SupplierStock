package com.example.supplierstock.data

import com.example.supplierstock.data.entities.Product

//todo пример, с добавлением рум стоит удалить
object ProductDatabase {
    val list = mutableListOf(
        Product(0, "Product A", 213.0, 1, "Supplier 1", "supplier1@email.com", "123-456-7890"),
        Product(1, "Product B", 16.0, 3, "Supplier 2", "supplier2@email.com", "987-654-3210")
    )
}