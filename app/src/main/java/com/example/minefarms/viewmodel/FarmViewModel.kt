package com.example.minefarms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minefarms.repository.FarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.minefarms.model.Farm

class FarmViewModel: ViewModel() {
    private val _farms = MutableStateFlow(emptyList<Farm>())
    val farms: StateFlow<List<Farm>> = _farms

    init {
        loadFarms()
    }

    private fun loadFarms() {
        viewModelScope.launch {
            _farms.value = FarmRepository.getFarms()
        }
    }
}