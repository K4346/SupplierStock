package com.example.supplierstock.ui.product_info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.data.entities.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductInfoScreen(
    viewModel: ProductInfoViewModel = viewModel(),
    productId: Int,
    navHostController: NavHostController

) {
    LaunchedEffect(Unit) {
        viewModel.initUiState(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.navigateToProductList.collect { navigate ->
            if (navigate) {
                navHostController.popBackStack()

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "SupplierStock",
            modifier = Modifier.padding(8.dp)
        )

        TextField(
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Product Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = uiState.price,
            onValueChange = { viewModel.onPriceChange(it) },
            label = { Text("Product price") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = uiState.quantity,
            onValueChange = { viewModel.onQuantityChange(it) },
            label = { Text("Product quantity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = uiState.supplier,
            onValueChange = { viewModel.onSupplierChange(it) },
            label = { Text("Supplier Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )

        TextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Supplier Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = uiState.phone,
            onValueChange = { viewModel.onPhoneChange(it) },
            label = { Text("Supplier Phone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
//                todo полностью передалть, перенести в вьюмодель
                if (uiState.name.isNotBlank()) {
                    val newProduct = Product(
                        id = viewModel.getId(),
                        name = uiState.name,
                        price = uiState.price.toDoubleOrNull() ?: 0.0,
                        quantity = uiState.quantity.toIntOrNull() ?: 0,
                        supplier = uiState.supplier,
                        email = uiState.email,
                        phone = uiState.phone
                    )
                    viewModel.editProductAndReturnBack(newProduct)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Save")
        }

    }
}