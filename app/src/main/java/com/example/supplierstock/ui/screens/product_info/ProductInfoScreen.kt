package com.example.supplierstock.ui.screens.product_info

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.R
import com.example.supplierstock.data.SettingsManager
import com.example.supplierstock.extensions.getActivity
import com.example.supplierstock.ui.composable.BasicTextField

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

    val activityContext = LocalContext.current.getActivity()

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
    val saveEncryptedFileResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.CreateDocument()
        ) { uri ->
            if (uri == null || activityContext == null) return@rememberLauncherForActivityResult
            val product = viewModel.makeProductForEncryptToFile()
            SettingsManager.saveToFile(
                activityContext,
                product,
                uri
            )
        }
    val loadEncryptedFileResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri ->
            if (uri == null || activityContext == null) return@rememberLauncherForActivityResult
            val product = SettingsManager.getProductFromEncryptedFile(
                activityContext,
                uri
            )
            viewModel.deserializeProduct(product)
        }
//    todo разделить функционал на 2 экрана
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.list_product)) },
                actions = {
                    if (productId == null) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_save),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    keyboardController?.hide()
                                    keyboardController?.hide()
                                    loadEncryptedFileResultLauncher.launch(arrayOf("application/json"))
                                }
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.baseline_upload),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .clickable {
                                    keyboardController?.hide()
                                    if (!viewModel.fieldsAreFine()) return@clickable
                                    saveEncryptedFileResultLauncher.launch(SettingsManager.encrypted_product_name)
                                }
                        )
                    }
                }
            )
        },
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
            BasicTextField(
                value = uiState.name,
                onValueChange = { viewModel.onNameChange(it) },
                stringResource(R.string.product_name),
            )
            BasicTextField(
                value = uiState.price,
                onValueChange = { viewModel.onPriceChange(it) },
                stringResource(R.string.price),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            BasicTextField(
                value = uiState.quantity,
                onValueChange = { viewModel.onQuantityChange(it) },
                stringResource(R.string.quantity),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            BasicTextField(
                value = uiState.supplier,
                onValueChange = { viewModel.onSupplierChange(it) },
                stringResource(R.string.supplier),
            )
            BasicTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                stringResource(R.string.email),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                visualTransformation = if (!viewModel.getFlagHideSensitiveData()) VisualTransformation.None else PasswordVisualTransformation()
            )
            BasicTextField(
                value = uiState.phone,
                onValueChange = { viewModel.onPhoneChange(it) },
                stringResource(R.string.phone),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                visualTransformation = if (!viewModel.getFlagHideSensitiveData()) VisualTransformation.None else PasswordVisualTransformation()
            )
            if (productId != null) {
                Text("${stringResource(R.string.data_source)}: ${uiState.dataSource} ")
            }

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
                        viewModel.removeTheCurrentProduct()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(text = stringResource(R.string.remove))
                }
            }
            if (productId != null) {
                Button(enabled = !viewModel.getFlagDisableDataSharing(),
                    onClick = { viewModel.shareData(activityContext) }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        modifier = Modifier.size(16.dp),
                        contentDescription = stringResource(id = R.string.share)
                    )
                }
            }
        }
    }
}