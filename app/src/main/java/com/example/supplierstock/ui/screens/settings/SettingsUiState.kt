package com.example.supplierstock.ui.screens.settings

data class SettingsUiState(
    val supplierName: String = "",
    val email: String = "",
    val phone: String = "",
    val useDefaultValues: Boolean = false,
    val hideSensitiveData: Boolean = false,
    val disableDataSending: Boolean = false
)
