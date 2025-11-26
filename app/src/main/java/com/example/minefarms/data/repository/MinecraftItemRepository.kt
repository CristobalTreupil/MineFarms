package com.example.minefarms.data.repository

import com.example.minefarms.data.remote.MinecraftApiService
import com.example.minefarms.data.remote.model.MinecraftItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para manejar operaciones relacionadas con items de Minecraft
 * Implementa el consumo de API REST externa según los requisitos de la evaluación
 */
class MinecraftItemRepository(
    private val apiService: MinecraftApiService
) {
    
    /**
     * Obtiene todos los items de Minecraft desde la API externa
     * @return Result con la lista de items o un error
     */
    suspend fun getAllItems(): Result<List<MinecraftItemResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllItems()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error al obtener items: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Obtiene un item específico por su ID
     * @param itemId ID del item a buscar
     * @return Result con el item o un error
     */
    suspend fun getItemById(itemId: String): Result<MinecraftItemResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getItemById(itemId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Item no encontrado"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    /**
     * Busca items por nombre (filtrado local)
     * @param query término de búsqueda
     * @return Result con la lista filtrada de items
     */
    suspend fun searchItems(query: String): Result<List<MinecraftItemResponse>> {
        return try {
            val allItemsResult = getAllItems()
            if (allItemsResult.isSuccess) {
                val items = allItemsResult.getOrNull() ?: emptyList()
                val filteredItems = items.filter { 
                    it.displayName.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
                }
                Result.success(filteredItems)
            } else {
                allItemsResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene items útiles para granjas (filtrado específico)
     * @return Result con items relacionados con granjas
     */
    suspend fun getFarmRelatedItems(): Result<List<MinecraftItemResponse>> {
        return try {
            val allItemsResult = getAllItems()
            if (allItemsResult.isSuccess) {
                val items = allItemsResult.getOrNull() ?: emptyList()
                // Filtrar items relevantes para granjas
                val farmKeywords = listOf(
                    "iron", "hopper", "chest", "redstone", "piston", 
                    "observer", "rail", "minecart", "comparator", "repeater",
                    "torch", "lever", "button", "pressure", "tripwire"
                )
                val farmItems = items.filter { item ->
                    farmKeywords.any { keyword ->
                        item.name.contains(keyword, ignoreCase = true) ||
                        item.displayName.contains(keyword, ignoreCase = true)
                    }
                }
                Result.success(farmItems)
            } else {
                allItemsResult
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
