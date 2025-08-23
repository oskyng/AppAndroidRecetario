package com.example.recetario.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recetario.ui.screen.LoginApp
import com.example.recetario.ui.screen.MainApp
import com.example.recetario.ui.screen.RecoveryPasswordApp
import com.example.recetario.ui.screen.RegisterApp

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.HOME) { MainApp() }
        composable(Routes.LOGIN){
            LoginApp(
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoForgot = { navController.navigate(Routes.FORGOT)},
                onGoHome = { navController.navigate(Routes.HOME) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                } }
            )
        }
        composable(Routes.REGISTER) { RegisterApp(onBack = { navController.popBackStack() }) }
        composable(Routes.FORGOT) { RecoveryPasswordApp(onBack = { navController.popBackStack() }) }
    }
}
