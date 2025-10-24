package com.example.minefarms.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorites",
    indices = [Index(value = ["userId", "farmId"], unique = true)]
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val farmId: Int, // ID de la granja predefinida
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val addedAt: Long = System.currentTimeMillis()
)
