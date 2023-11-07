package com.example.supplierstock

import androidx.navigation.NavHostController


class AppNavigation(private val navController: NavHostController) {
    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String, inclusive_: Boolean = true) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = inclusive_ }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    companion object {
        const val STOCK_LIST_SCREEN = "StockListScreen"
        const val PRODUCT_SCREEN = "LoginScreen"
        const val ADD_PRODUCT_SCREEN = "AddProductScreen"
    }
}