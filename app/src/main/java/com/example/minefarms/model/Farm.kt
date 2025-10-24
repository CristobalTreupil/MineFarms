package com.example.minefarms.model

data class Farm(
    val id: Int,
    val name: String,
    val description: String,
    val materials: List<String>,
    val difficulty: String,
    val production: String,
    val productionRate: String,
    val process: String,
    val tutorialUrl: String,
    val imageResourceName: String,
    val tags: List<String>
)