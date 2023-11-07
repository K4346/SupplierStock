package com.example.supplierstock.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierStockScreen(openAndPopUp: (String, String) -> Unit) {
    var products by remember { mutableStateOf(dummyProductList) }
    var newProductName by remember { mutableStateOf("") }
    var newProductPrice by remember { mutableStateOf("") }
    var newProductQuantity by remember { mutableStateOf("") }
    var newProductSupplier by remember { mutableStateOf("") }
    var newProductEmail by remember { mutableStateOf("") }
    var newProductPhone by remember { mutableStateOf("") }

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
            value = newProductName,
            onValueChange = { newProductName = it },
            label = { Text("Product Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = newProductPrice,
            onValueChange = { newProductPrice = it },
            label = { Text("Product price") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TextField(
            value = newProductQuantity,
            onValueChange = { newProductQuantity = it },
            label = { Text("Product quantity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = newProductSupplier,
            onValueChange = { newProductSupplier = it },
            label = { Text("Supplier Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )

        TextField(
            value = newProductEmail,
            onValueChange = { newProductEmail = it },
            label = { Text("Supplier Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        TextField(
            value = newProductPhone,
            onValueChange = { newProductPhone = it },
            label = { Text("Supplier Phone") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {
                if (newProductName.isNotBlank()) {
                    val newProduct = Product(
                        id = getId(products),
                        name = newProductName,
                        price = newProductPrice.toDoubleOrNull() ?: 0.0,
                        quantity = newProductQuantity.toIntOrNull() ?: 0,
                        supplier = newProductSupplier,
                        email = newProductEmail,
                        phone = newProductPhone
                    )
                    products = products + newProduct
                    newProductName = ""
                    newProductSupplier = ""
                    newProductEmail = ""
                    newProductPhone = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Add Product")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(products) { product ->
                ProductItem(product = product)
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
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

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val supplier: String,
    val email: String,
    val phone: String
)

val dummyProductList = listOf(
    Product(0, "Product A", 213.0, 1, "Supplier 1", "supplier1@email.com", "123-456-7890"),
    Product(1, "Product B", 16.0, 3, "Supplier 2", "supplier2@email.com", "987-654-3210")
)

fun getId(products: List<Product>) = products.maxOf { it.id } + 1