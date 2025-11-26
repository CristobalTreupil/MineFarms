package com.example.minefarms.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para almacenar granjas en la base de datos local
 * Implementa persistencia interna según requisitos de evaluación
 */
@Entity(tableName = "farms")
data class FarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val materials: String, // JSON string de la lista de materiales
    val difficulty: String,
    val production: String,
    val productionRate: String,
    val process: String,
    val tutorialUrl: String,
    val imageResourceName: String,
    val tags: String, // JSON string de la lista de tags
    val isCustom: Boolean = false, // Para distinguir granjas predefinidas de las creadas por el usuario
    val createdBy: Long? = null, // ID del usuario que creó la granja (null para predefinidas)
    val imageUri: String? = null // URI de imagen de galería (para granjas de usuario)
)
