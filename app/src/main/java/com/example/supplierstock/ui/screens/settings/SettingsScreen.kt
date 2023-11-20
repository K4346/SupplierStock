package com.example.supplierstock.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.R
import com.example.supplierstock.ui.composable.BasicTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(),
    navHostController: NavHostController
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.initUiState()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {

            BasicTextField(
                value = uiState.supplierName,
                onValueChange = { viewModel.onSupplierNameChange(it) },
                label = stringResource(id = R.string.default_supplier_name),
            )
            BasicTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = stringResource(id = R.string.default_email),
            )
            BasicTextField(
                value = uiState.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                label = stringResource(id = R.string.default_phone),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.useDefaultValues,
                    onCheckedChange = { viewModel.onUseDefaultValuesChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.use_default_values))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.hideSensitiveData,
                    onCheckedChange = { viewModel.onHideSensitiveDataChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.hide_sensitive_data))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = uiState.disableDataSending,
                    onCheckedChange = { viewModel.onDisableDataSendingChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = stringResource(id = R.string.disable_data_sending))
            }

            // Save button
            Button(
                onClick = {
                    viewModel.saveSettings()
                    navHostController.popBackStack()
                },
            ) {
                Text(text = stringResource(id = R.string.save))
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SettingsScreenPreview() {
//    SettingsScreen()
//}