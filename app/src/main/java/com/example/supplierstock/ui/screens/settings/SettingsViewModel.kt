package com.example.supplierstock.ui.screens.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.supplierstock.data.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {


    fun initUiState() {
        _uiState.value = SettingsUiState(
            supplierName = SettingsManager.defaultSupplierName,
            email = SettingsManager.defaultEmail,
            phone = SettingsManager.defaultPhone,
            useDefaultValues = SettingsManager.useDefaultValues,
            hideSensitiveData = SettingsManager.hideSensitiveData,
            disableDataSending = SettingsManager.disableDataSharing
        )
    }

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun onSupplierNameChange(newValue: String) {
        _uiState.value = _uiState.value.copy(supplierName = newValue)
    }

    fun onEmailChange(newValue: String) {
        _uiState.value = _uiState.value.copy(email = newValue)
    }

    fun onPhoneChange(newValue: String) {
        _uiState.value = _uiState.value.copy(phone = newValue)
    }

    fun onUseDefaultValuesChange(newValue: Boolean) {
        _uiState.value = _uiState.value.copy(useDefaultValues = newValue)
    }

    fun onHideSensitiveDataChange(newValue: Boolean) {
        _uiState.value = _uiState.value.copy(hideSensitiveData = newValue)
    }

    fun onDisableDataSendingChange(newValue: Boolean) {
        _uiState.value = _uiState.value.copy(disableDataSending = newValue)
    }

    fun saveSettings() {
        with(SettingsManager) {
            defaultSupplierName = uiState.value.supplierName
            defaultEmail = uiState.value.email
            defaultPhone = uiState.value.phone
            useDefaultValues = uiState.value.useDefaultValues
            hideSensitiveData = uiState.value.hideSensitiveData
            disableDataSharing = uiState.value.disableDataSending
        }
    }
}