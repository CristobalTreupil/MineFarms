package com.example.minefarms.data.remote

import com.example.minefarms.data.remote.model.MinecraftItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Servicio de API para obtener información de items de Minecraft
 * Usa la API pública de Minecraft
 */
interface MinecraftApiService {
    
    /**
     * Obtiene lista de todos los items de Minecraft
     */
    @GET("items")
    suspend fun getAllItems(): Response<List<MinecraftItemResponse>>
    
    /**
     * Obtiene información detallada de un item específico
     */
    @GET("item/{itemId}")
    suspend fun getItemById(@Path("itemId") itemId: String): Response<MinecraftItemResponse>
}
