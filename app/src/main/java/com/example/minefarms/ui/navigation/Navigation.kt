package com.example.minefarms.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minefarms.ui.screens.FarmDetailScreen
import com.example.minefarms.ui.screens.FarmListScreen
import com.example.minefarms.viewmodel.FarmViewModel

@Composable
fun AppNavigation(viewModel: FarmViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "farmList") {
        composable("farmList") {
            FarmListScreen(viewModel) {
                navController.navigate("farmDetail/$it")
            }
        }
        composable("farmDetail/{farmId}") { backStackEntry ->
            val farmId = backStackEntry.arguments?.getString("farmId")?.toIntOrNull() ?: 0
            FarmDetailScreen(farmId) // ✅ Ya no pasas viewModel
        }
    }
}