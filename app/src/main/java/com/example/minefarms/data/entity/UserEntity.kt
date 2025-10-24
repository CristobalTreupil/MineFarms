package com.example.minefarms.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val passwordHash: String, // En producción usar hash apropiado
    val displayName: String,
    val createdAt: Long = System.currentTimeMillis(),
    val avatarEmoji: String = "👤" // Emoji como avatar simple
)
