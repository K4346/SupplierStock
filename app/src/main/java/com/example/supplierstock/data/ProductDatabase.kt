package com.example.supplierstock.data

import com.example.supplierstock.data.entities.ProductEntity

//todo пример, с добавлением рум стоит удалить
object ProductDatabase {
    val list = mutableListOf(
        ProductEntity(0, "ProductA", 213.0, 1, "Supplier 1", "supplier1@email.com", "+79186557713"),
        ProductEntity(1, "ProductB", 16.0, 3, "Supplier 2", "supplier2@email.com", "+79186599913")
    )
}