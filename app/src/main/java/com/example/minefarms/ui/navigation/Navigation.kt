package com.example.minefarms.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minefarms.ui.screens.CreateFarmScreen
import com.example.minefarms.ui.screens.FarmDetailScreen
import com.example.minefarms.ui.screens.FarmListScreen
import com.example.minefarms.ui.screens.ProfileScreen
import com.example.minefarms.ui.screens.auth.LoginScreen
import com.example.minefarms.ui.screens.auth.RegisterScreen
import com.example.minefarms.ui.viewmodel.AuthViewModel
import com.example.minefarms.viewmodel.FarmViewModel

@Composable
fun AppNavigation(
    farmViewModel: FarmViewModel,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()
    
    // Determinar la pantalla inicial según el estado de autenticación
    val startDestination = if (authState.isLoggedIn) "farmList" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("farmList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                authViewModel = authViewModel
            )
        }
        
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("farmList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                authViewModel = authViewModel
            )
        }
        
        composable("farmList") {
            FarmListScreen(
                viewModel = farmViewModel,
                onFarmClick = { farmId ->
                    navController.navigate("farmDetail/$farmId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onCreateFarmClick = {
                    navController.navigate("createFarm")
                }
            )
        }
        
        composable("createFarm") {
            CreateFarmScreen(
                onBack = { navController.popBackStack() },
                onFarmCreated = {
                    navController.navigate("profile") {
                        popUpTo("farmList") { inclusive = false }
                    }
                },
                authViewModel = authViewModel
            )
        }
        
        composable("farmDetail/{farmId}") { backStackEntry ->
            val farmId = backStackEntry.arguments?.getString("farmId")?.toLongOrNull() ?: 0L
            FarmDetailScreen(
                farmId = farmId,
                farmViewModel = farmViewModel,
                onBack = { navController.popBackStack() },
                authViewModel = authViewModel
            )
        }
        
        composable("profile") {
            ProfileScreen(
                authViewModel = authViewModel,
                onBack = {
                    if (authState.isLoggedIn) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("login") {
                            popUpTo("farmList") { inclusive = true }
                        }
                    }
                },
                onNavigateToFarm = { farmId ->
                    navController.navigate("farmDetail/$farmId")
                }
            )
        }
    }
}