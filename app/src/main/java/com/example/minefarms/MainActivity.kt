package com.example.minefarms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minefarms.ui.navigation.AppNavigation
import com.example.minefarms.ui.theme.MineFarmsTheme
import com.example.minefarms.viewmodel.FarmViewModel

// Creamos un CompositionLocal para pasar el AssetServer a través de Compose
val LocalAssetServer = staticCompositionLocalOf<AssetServer> { error("AssetServer not provided") }

class MainActivity : ComponentActivity() {
    private lateinit var assetServer: AssetServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assetServer = AssetServer(this)
        assetServer.start() // Inicia el servidor

        setContent {
            MineFarmsTheme {
                // Proporcionamos el AssetServer como Local
                CompositionLocalProvider(LocalAssetServer provides assetServer) {
                    AppNavigation(viewModel = viewModel())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        assetServer.stop() // Asegúrate de detenerlo al salir
    }
}