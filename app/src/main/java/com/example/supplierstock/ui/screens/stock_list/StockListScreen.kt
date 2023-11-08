package com.example.supplierstock.ui.screens.stock_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.supplierstock.SupplierStockScreenRoutes
import com.example.supplierstock.data.entities.Product


@Composable
fun SupplierStockScreen(
    viewModel: StockListViewModel = viewModel(),
    navController: NavHostController
) {
    LazyColumn {
        items(viewModel.products) { product ->
            ProductItem(product = product, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(product: Product, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = {
//            todo переместить
            navController.navigate("${SupplierStockScreenRoutes.ProductScreen.name}/${product.id}")
        }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
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
