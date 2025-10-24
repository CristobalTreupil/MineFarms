package com.example.minefarms.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MinecraftGreen60,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = MinecraftGreen80,
    onPrimaryContainer = MinecraftGreen40,
    
    secondary = MinecraftDirt60,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = MinecraftDirt80,
    onSecondaryContainer = MinecraftDirt40,
    
    tertiary = MinecraftGold60,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    tertiaryContainer = MinecraftGold80,
    onTertiaryContainer = MinecraftGold40,
    
    error = MinecraftRedstone60,
    onError = androidx.compose.ui.graphics.Color.White,
    errorContainer = MinecraftRedstone80,
    onErrorContainer = MinecraftRedstone40,
    
    background = MinecraftSkyLight,
    onBackground = MinecraftStone40,
    
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = MinecraftStone40,
    surfaceVariant = MinecraftStone80,
    onSurfaceVariant = MinecraftStone60,
)

private val DarkColorScheme = darkColorScheme(
    primary = MinecraftGreen80,
    onPrimary = MinecraftGreen40,
    primaryContainer = MinecraftGreen40,
    onPrimaryContainer = MinecraftGreen80,
    
    secondary = MinecraftDirt80,
    onSecondary = MinecraftDirt40,
    secondaryContainer = MinecraftDirt40,
    onSecondaryContainer = MinecraftDirt80,
    
    tertiary = MinecraftGold80,
    onTertiary = MinecraftGold40,
    tertiaryContainer = MinecraftGold40,
    onTertiaryContainer = MinecraftGold80,
    
    error = MinecraftRedstone80,
    onError = MinecraftRedstone40,
    errorContainer = MinecraftRedstone40,
    onErrorContainer = MinecraftRedstone80,
    
    background = MinecraftCave,
    onBackground = MinecraftStone80,
    
    surface = MinecraftStone40,
    onSurface = MinecraftStone80,
    surfaceVariant = MinecraftStone60,
    onSurfaceVariant = MinecraftStone80,
)

@Composable
fun MineFarmsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Deshabilitado para usar nuestros colores
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}