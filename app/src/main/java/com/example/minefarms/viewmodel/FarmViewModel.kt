package com.example.minefarms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.minefarms.data.remote.RetrofitClient
import com.example.minefarms.data.repository.MinecraftItemRepository
import com.example.minefarms.repository.FarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.minefarms.model.Farm
import com.example.minefarms.data.remote.model.MinecraftItemResponse

/**
 * ViewModel para manejar la lógica de granjas
 * Sigue arquitectura MVVM - no contiene lógica de datos, solo coordina entre UI y Repository
 */
class FarmViewModel(
    private val farmRepository: FarmRepository
) : ViewModel() {
    
    // StateFlow para la lista de granjas desde la BD local
    val farms: StateFlow<List<Farm>> = farmRepository.getAllFarms()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    // Repository para API externa
    private val minecraftItemRepository = MinecraftItemRepository(RetrofitClient.minecraftApiService)
    
    // StateFlow para items de Minecraft desde API externa
    private val _minecraftItems = MutableStateFlow<List<MinecraftItemResponse>>(emptyList())
    val minecraftItems: StateFlow<List<MinecraftItemResponse>> = _minecraftItems
    
    // StateFlow para items relacionados con granjas
    private val _farmRelatedItems = MutableStateFlow<List<MinecraftItemResponse>>(emptyList())
    val farmRelatedItems: StateFlow<List<MinecraftItemResponse>> = _farmRelatedItems
    
    // Estado de carga de API
    private val _isLoadingItems = MutableStateFlow(false)
    val isLoadingItems: StateFlow<Boolean> = _isLoadingItems
    
    // Mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    
    // Estado de inicialización
    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized
    
    // Cache de StateFlows por farmId para evitar recreaciones
    private val farmByIdCache = mutableMapOf<Long, StateFlow<Farm?>>()

    init {
        initializeDefaultFarms()
        loadMinecraftItems()
        loadFarmRelatedItems()
    }
    
    /**
     * Inicializa las granjas predefinidas si la BD está vacía
     */
    private fun initializeDefaultFarms() {
        viewModelScope.launch {
            farmRepository.initializeDefaultFarms()
            _isInitialized.value = true
        }
    }
    
    /**
     * Carga items de Minecraft desde la API externa
     * Implementa consumo de API REST según requisitos
     */
    fun loadMinecraftItems() {
        viewModelScope.launch {
            _isLoadingItems.value = true
            _errorMessage.value = null
            
            val result = minecraftItemRepository.getAllItems()
            result.onSuccess { items ->
                _minecraftItems.value = items
            }.onFailure { error ->
                _errorMessage.value = "Error al cargar items: ${error.message}"
            }
            
            _isLoadingItems.value = false
        }
    }
    
    /**
     * Carga items relacionados con granjas desde la API externa
     */
    fun loadFarmRelatedItems() {
        viewModelScope.launch {
            val result = minecraftItemRepository.getFarmRelatedItems()
            result.onSuccess { items ->
                _farmRelatedItems.value = items
            }.onFailure { error ->
                _errorMessage.value = "Error al cargar items de granjas: ${error.message}"
            }
        }
    }
    
    /**
     * Busca granjas en la BD local
     */
    fun searchFarms(query: String): StateFlow<List<Farm>> {
        return farmRepository.searchFarms(query)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }
    
    /**
     * Busca items de Minecraft por nombre
     */
    fun searchMinecraftItems(query: String) {
        viewModelScope.launch {
            _isLoadingItems.value = true
            val result = minecraftItemRepository.searchItems(query)
            result.onSuccess { items ->
                _minecraftItems.value = items
            }.onFailure { error ->
                _errorMessage.value = "Error en búsqueda: ${error.message}"
            }
            _isLoadingItems.value = false
        }
    }
    
    /**
     * Obtiene una granja específica por ID
     * Usa cache para evitar crear múltiples StateFlows del mismo farmId
     */
    fun getFarmById(farmId: Long): StateFlow<Farm?> {
        return farmByIdCache.getOrPut(farmId) {
            farmRepository.getFarmById(farmId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = null
                )
        }
    }
    
    /**
     * Limpia mensajes de error
     */
    fun clearError() {
        _errorMessage.value = null
    }
}