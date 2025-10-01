package com.example.minefarms.repository

import com.example.minefarms.model.Farm
// No necesitas importar R.raw aquí

object FarmRepository {
    fun getFarms(): List<Farm> {
        return listOf(
            Farm(
                id = 1,
                name = "Granja de Hierro",
                description = "Una granja automatizada que usa aldeanos y zombis para generar gólems de hierro, los cuales son eliminados para obtener lingotes y amapolas.",
                process = "Los aldeanos detectan al zombi y generan gólems de hierro. Estos son guiados hacia una cámara de muerte con lava o caída. Los drops caen en tolvas y son almacenados en cofres.",
                purpose = "Producción infinita de hierro sin minería, ideal para herramientas, rieles y construcciones masivas.",
                tutorialUrl = "https://youtu.be/XXXXX", // Quita los espacios al final
                modelFileName = "granja_hierro" // ✅ Solo el nombre del archivo (sin extensión)
            )
        )
    }
}