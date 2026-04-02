package com.example.agrocalc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.agrocalc.ui.home.HomeScreen
import com.example.agrocalc.ui.session.SessionScreen
import com.example.agrocalc.ui.truck.TruckEntryScreen
import com.example.agrocalc.ui.history.HistoryScreen
import com.example.agrocalc.ui.settings.SettingsScreen
import com.example.agrocalc.viewmodel.AgroCalcViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: AgroCalcViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            "session/{sesionId}/{productoNombre}",
            arguments = listOf(
                navArgument("sesionId") { type = NavType.IntType },
                navArgument("productoNombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            SessionScreen(
                navController = navController,
                viewModel = viewModel,
                sesionId = backStackEntry.arguments?.getInt("sesionId") ?: 0,
                productoNombre = backStackEntry.arguments?.getString("productoNombre") ?: ""
            )
        }
        composable(
            "truck/{sesionId}",
            arguments = listOf(navArgument("sesionId") { type = NavType.IntType })
        ) { backStackEntry ->
            TruckEntryScreen(
                navController = navController,
                viewModel = viewModel,
                sesionId = backStackEntry.arguments?.getInt("sesionId") ?: 0
            )
        }
        composable("history") {
            HistoryScreen(navController = navController, viewModel = viewModel)
        }
        composable("settings") {
            SettingsScreen(navController = navController, viewModel = viewModel)
        }
    }
}
