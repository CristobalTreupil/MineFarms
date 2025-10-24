package com.example.minefarms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minefarms.ui.navigation.AppNavigation
import com.example.minefarms.ui.theme.MineFarmsTheme
import com.example.minefarms.ui.viewmodel.AuthViewModel
import com.example.minefarms.viewmodel.FarmViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MineFarmsTheme {
                val farmViewModel: FarmViewModel = viewModel()
                val authViewModel: AuthViewModel = viewModel()
                
                AppNavigation(
                    farmViewModel = farmViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}