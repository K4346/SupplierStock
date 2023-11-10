package com.example.supplierstock.domain.use_cases

import android.util.Patterns

class ValidatorUseCase {
    private val MIN_NAME_LENGTH = 3
    fun validateProductName(name: String): String? {
        return if (name.isNotBlank() && name.length >= MIN_NAME_LENGTH && !name.contains(" ")) null
        else {
            "Имя продукта должно быть больше ${MIN_NAME_LENGTH} символов и не иметь пробелов"
        }
    }

    fun validateSupplierName(name: String): String? {
        return if (name.isNotBlank() && name.length >= 3) null
        else {
            "Имя поставщика должно быть длинее ${MIN_NAME_LENGTH} символов"
        }

    }

    fun validatePrice(price: String): String? {
        return if (price.isNotBlank() && price.toDoubleOrNull() != null && !price.contains(" ") && price.toDouble() > 0.0) null
        else {
            "Некорректная цена"
        }
    }

    fun validateQuantity(quantity: String): String? {
        return if (quantity.isNotBlank() && !quantity.contains(" ") && quantity.toDoubleOrNull() != null && quantity.toDouble()
                .toInt().toDouble() == quantity.toDouble() && quantity.toDouble() > 0.0
        ) null
        else {
            "Некорректное количество товара"
        }
    }

    fun validateEmail(email: String): String? {
        return if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() && !email.contains(" ")
        ) null
        else {
            "Некорректная почта"
        }
    }

    fun validatePhone(phone: String): String? {
        return if (phone.isNotBlank() && Patterns.PHONE.matcher(phone)
                .matches() && phone.startsWith("+7") && phone.length == 12 && !phone.contains(" ")
        ) null
        else {
            "Номер телефона не соответствует номеру РФ"
        }
    }

}