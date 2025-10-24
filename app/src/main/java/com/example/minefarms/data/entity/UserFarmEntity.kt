package com.example.minefarms.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_farms")
data class UserFarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val name: String,
    val description: String,
    val materials: String = "", // Materiales separados por coma
    val difficulty: String = "Media",
    val production: String = "",
    val tutorialUrl: String? = null,
    val imageUri: String? = null, // URI de la imagen de galería
    val createdAt: Long = System.currentTimeMillis()
)
