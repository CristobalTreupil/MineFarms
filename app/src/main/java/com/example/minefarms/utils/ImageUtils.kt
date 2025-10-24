package com.example.minefarms.utils

import android.content.Context

/**
 * Utilidad para obtener el ID de recurso drawable a partir del nombre
 */
object ImageUtils {
    /**
     * Obtiene el ID del recurso drawable para una imagen
     * @param context Contexto de la aplicación
     * @param imageName Nombre del recurso sin extensión (ej: "farm_iron_golem")
     * @return ID del recurso o 0 si no se encuentra
     */
    fun getDrawableId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        )
    }
    
    /**
     * Verifica si existe una imagen con el nombre dado
     * @param context Contexto de la aplicación
     * @param imageName Nombre del recurso sin extensión
     * @return true si existe, false si no
     */
    fun imageExists(context: Context, imageName: String): Boolean {
        return getDrawableId(context, imageName) != 0
    }
}
