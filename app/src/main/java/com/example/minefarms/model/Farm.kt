package com.example.minefarms.model

/**
 * Modelo de dominio para representar una granja de Minecraft
 * Usado en la capa de UI según arquitectura MVVM
 */
data class Farm(
    val id: Long,
    val name: String,
    val description: String,
    val materials: List<String>,
    val difficulty: String,
    val production: String,
    val productionRate: String,
    val process: String,
    val tutorialUrl: String,
    val imageResourceName: String,
    val tags: List<String>,
    val imageUri: String? = null // URI de imagen de galería para granjas de usuario
)