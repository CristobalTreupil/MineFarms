package com.example.minefarms.utils

import android.content.Context

object ImageUtils {
    private val imageCache = mutableMapOf<String, Int>()
    
    fun getDrawableId(context: Context, imageName: String): Int {
        if (imageName.isBlank()) return 0
        
        imageCache[imageName]?.let { return it }
        
        val resourceId = try {
            context.resources.getIdentifier(imageName, "drawable", context.packageName)
        } catch (e: Exception) {
            0
        }
        
        if (resourceId != 0) {
            imageCache[imageName] = resourceId
        }
        return resourceId
    }
    
    fun clearCache() {
        imageCache.clear()
    }
}
