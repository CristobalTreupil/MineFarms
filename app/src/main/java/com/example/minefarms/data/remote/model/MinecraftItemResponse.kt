package com.example.minefarms.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos para items de Minecraft desde la API externa
 */
data class MinecraftItemResponse(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("displayName")
    val displayName: String,
    
    @SerializedName("stackSize")
    val stackSize: Int? = 64,
    
    @SerializedName("enchantCategories")
    val enchantCategories: List<String>? = null,
    
    @SerializedName("maxDurability")
    val maxDurability: Int? = null,
    
    @SerializedName("repairWith")
    val repairWith: List<String>? = null
)

/**
 * Modelo para el resultado de la búsqueda de items
 */
data class ItemSearchResult(
    val items: List<MinecraftItemResponse>,
    val total: Int
)
