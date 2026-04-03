package com.example.agrocalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.agrocalc.ui.ParticleBackground
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
                Box(modifier = Modifier.fillMaxSize()) {
                    ParticleBackground()
                    val navController = rememberNavController()
                    AppNavigation(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}