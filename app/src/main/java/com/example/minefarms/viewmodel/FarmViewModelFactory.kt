package com.example.minefarms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.minefarms.repository.FarmRepository

/**
 * Factory para crear instancias de FarmViewModel con dependencias
 * Necesario porque FarmViewModel requiere un FarmRepository en su constructor
 */
class FarmViewModelFactory(
    private val farmRepository: FarmRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FarmViewModel::class.java)) {
            return FarmViewModel(farmRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
