package com.example.supplierstock

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.supplierstock.ui.screens.SupplierStockScreen


//todo руты можно вынести
@Composable
fun SupplierStockAppNavigation() {
    val navController = rememberNavController()
    val appNavigation = AppNavigation(navController)

    NavHost(
        navController = navController,
        startDestination = AppNavigation.STOCK_LIST_SCREEN,
        //  enterTransition =   { slideInHorizontally(animationSpec = tween(700)) },
        // exitTransition =   { slideOutHorizontally(animationSpec = tween(700)) }
    ) {
        composable(AppNavigation.STOCK_LIST_SCREEN) {
            SupplierStockScreen { route, popUp -> appNavigation.navigateAndPopUp(route, popUp) }
        }
//        composable(LOGIN_SCREEN) {
//            LoginScreen({ route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
//        }
//        composable(SIGN_UP_SCREEN) {
//            SignUpScreen({ route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
//        }
//        composable(USER_INFO_SCREEN) {
//            UserInfoScreen({ route, popUp -> appNavigation.navigateAndPopUp(route, popUp) })
//        }
//        composable(MAIN_SCREEN) {
//            MainScreen({ route -> appNavigation.navigate(route) })
//        }
    }
}
