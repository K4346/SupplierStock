package com.example.supplierstock.ui.screens.stock_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.R
import com.example.supplierstock.SupplierStockScreenRoutes
import com.example.supplierstock.data.entities.ProductEntity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierStockScreen(
    viewModel: StockListViewModel = viewModel(), navController: NavHostController
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.stock)) }, actions = {
            IconButton(onClick = { goToSettingsScreen(navController) }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings)
                )
            }
        })
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(contentPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val productsState =
                viewModel.products.collectAsStateWithLifecycle(initialValue = emptyList())
            IconButton(onClick = { goToAddNewProductScreen(navController) }) {
                Icon(
                    painter = painterResource(R.drawable.baseline_add_24),
                    modifier = Modifier.size(100.dp),
                    contentDescription = stringResource(id = R.string.add)
                )
            }
            LazyColumn {
                items(productsState.value) { product ->
                    ProductItem(product = product, navController)
                }
            }
        }
    }
}

fun goToAddNewProductScreen(navController: NavHostController) {
    navController.navigate(SupplierStockScreenRoutes.AddProductScreen.name)
}

fun goToSettingsScreen(navController: NavHostController) {
    navController.navigate(SupplierStockScreenRoutes.SettingsScreen.name)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: ProductEntity, navController: NavHostController) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = {
            navController.navigate("${SupplierStockScreenRoutes.ProductScreen.name}/${product.id}")
        }) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = "Product: ${product.name}",
            )
            Text(
                text = "Price: ${product.price}",
            )
            Text(
                text = "Quantity: ${product.quantity}",
            )
            Text(
                text = "Supplier: ${product.supplier}",
            )
            Text(
                text = "Email: ${product.email}",
            )
            Text(
                text = "Phone: ${product.phone}",
            )
        }
    }
}
