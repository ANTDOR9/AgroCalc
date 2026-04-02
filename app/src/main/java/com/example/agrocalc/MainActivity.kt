package com.example.agrocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.agrocalc.ui.navigation.AppNavigation
import com.example.agrocalc.ui.theme.AgroCalcTheme
import com.example.agrocalc.viewmodel.AgroCalcViewModel
import com.example.agrocalc.viewmodel.AgroCalcViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: AgroCalcViewModel by viewModels {
        AgroCalcViewModelFactory((application as AgroCalcApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgroCalcTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}