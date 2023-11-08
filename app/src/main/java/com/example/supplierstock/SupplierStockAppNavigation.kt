package com.example.supplierstock

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.supplierstock.ui.product_info.ProductInfoScreen
import com.example.supplierstock.ui.screens.stock_list.SupplierStockScreen


@Composable
fun SupplierStockAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SupplierStockScreenRoutes.StockListScreen.name
    ) {

        composable(SupplierStockScreenRoutes.StockListScreen.name) {
            SupplierStockScreen(navController = navController)
        }
        composable("${SupplierStockScreenRoutes.ProductScreen.name}/{productId}") { backStackEntry ->
            val productId =
                backStackEntry.arguments?.getString("productId")?.toInt() ?: return@composable
            ProductInfoScreen(productId = productId, navHostController = navController)
        }
    }
}

enum class SupplierStockScreenRoutes {
    StockListScreen,
    ProductScreen,
    AddProductScreen
}