package com.example.minefarms.model

data class Farm (
    val id: Int,
    val name: String,
    val description: String,
    val process: String,
    val purpose: String,
    val tutorialUrl: String,
    val modelFileName: String // ✅ Cambia el nombre y tipo del campo
)