package com.example.minefarms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.repository.FarmRepository
import com.example.minefarms.ui.navigation.AppNavigation
import com.example.minefarms.ui.theme.MineFarmsTheme
import com.example.minefarms.ui.viewmodel.AuthViewModel
import com.example.minefarms.viewmodel.FarmViewModel
import com.example.minefarms.viewmodel.FarmViewModelFactory

/**
 * Actividad principal de la aplicación
 * Inicializa la base de datos, repositorios y ViewModels según arquitectura MVVM
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Inicializar base de datos
        val database = AppDatabase.getDatabase(applicationContext)
        
        // Crear repositorio con DAO
        val farmRepository = FarmRepository(database.farmDao())
        
        setContent {
            MineFarmsTheme {
                // Crear ViewModels con factories
                val farmViewModel: FarmViewModel = viewModel(
                    factory = FarmViewModelFactory(farmRepository)
                )
                val authViewModel: AuthViewModel = viewModel()
                
                AppNavigation(
                    farmViewModel = farmViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}