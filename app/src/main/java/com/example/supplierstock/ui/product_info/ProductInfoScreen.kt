package com.example.supplierstock.ui.product_info

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.R
import com.example.supplierstock.ui.composable.ProductInfoBasicTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProductInfoScreen(
    viewModel: ProductInfoViewModel = viewModel(),
    productId: Int?,
    navHostController: NavHostController

) {

    LaunchedEffect(Unit) {
        if (productId != null) {
            viewModel.initUiState(productId)
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateToProductList.collect { navigate ->
            if (navigate) {
                navHostController.popBackStack()
            }
        }
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(UInt) {
        viewModel.errorSF.collect { error ->
            if (!viewModel.showError || error == null) return@collect
            Log.i("kpop", error)
            val result = snackbarHostState
                .showSnackbar(
                    message = error,
                    duration = SnackbarDuration.Short
                )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                }

                SnackbarResult.Dismissed -> {
                    viewModel.showError = false
                }
            }
        }

    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.list_product),
                modifier = Modifier.padding(8.dp)
            )
            ProductInfoBasicTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                stringResource(R.string.product_name),
                Modifier.padding(contentPadding)
            )
            ProductInfoBasicTextField(
                value = uiState.price,
                onValueChange = { viewModel.onPriceChange(it) },
                stringResource(R.string.price),
                Modifier.padding(contentPadding),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            ProductInfoBasicTextField(
                value = uiState.quantity,
                onValueChange = { viewModel.onQuantityChange(it) },
                stringResource(R.string.quantity),
                Modifier.padding(contentPadding),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            ProductInfoBasicTextField(
                value = uiState.supplier,
                onValueChange = { viewModel.onSupplierChange(it) },
                stringResource(R.string.supplier),
                Modifier.padding(contentPadding)
            )
            ProductInfoBasicTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                stringResource(R.string.email),
                Modifier.padding(contentPadding),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            ProductInfoBasicTextField(
                value = uiState.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                stringResource(R.string.phone),
                Modifier.padding(contentPadding),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Button(
                onClick = {
                    keyboardController?.hide()
                    if (viewModel.fieldsAreFine()) {
                        viewModel.processProduct()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                if (productId == null) {
                    Text(text = stringResource(R.string.add))
                } else {
                    Text(text = stringResource(R.string.save))
                }
            }
            if (productId != null) {
                Button(
                    onClick = {
                        viewModel.removeProduct()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = stringResource(R.string.remove))
                }
            }
        }
    }
}