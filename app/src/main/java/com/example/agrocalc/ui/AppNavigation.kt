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
import androidx.compose.runtime.*

import com.example.agrocalc.data.Camion
import kotlinx.coroutines.launch

import com.example.agrocalc.ui.session.ChartScreen
import com.example.agrocalc.ui.session.ChartScreen


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


        composable(
            "editTruck/{camionId}",
            arguments = listOf(navArgument("camionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val camionId = backStackEntry.arguments?.getInt("camionId") ?: 0
            var camion by remember { mutableStateOf<Camion?>(null) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(camionId) {
                scope.launch {
                    camion = viewModel.getCamionById(camionId)
                }
            }

            camion?.let {
                com.example.agrocalc.ui.truck.EditTruckScreen(
                    navController = navController,
                    viewModel = viewModel,
                    camion = it
                )
            }
        }
        composable(
            "chart/{sesionId}/{productoNombre}",
            arguments = listOf(
                navArgument("sesionId") { type = NavType.IntType },
                navArgument("productoNombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ChartScreen(
                navController = navController,
                viewModel = viewModel,
                sesionId = backStackEntry.arguments?.getInt("sesionId") ?: 0,
                productoNombre = backStackEntry.arguments?.getString("productoNombre") ?: ""
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