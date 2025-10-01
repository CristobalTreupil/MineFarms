package com.example.minefarms.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build // Importa Build para verificar la versión
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.minefarms.LocalAssetServer // Importa el Local
import com.example.minefarms.model.Farm
import com.example.minefarms.repository.FarmRepository
import java.io.ByteArrayOutputStream

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(farmId: Int) {
    val farm = FarmRepository.getFarms().find { it.id == farmId } ?: return
    val assetServer = LocalAssetServer.current // Obtiene el servidor local

    var rotation by remember { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    // Registrar sensor de giroscopio
    DisposableEffect(rotationVectorSensor) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                rotation = event.values[0] * 180 / Math.PI.toFloat()
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(listener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose { sensorManager.unregisterListener(listener) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(farm.name) }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(farm.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Modelo 3D usando WebView y model-viewer
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true // Necesario para model-viewer
                        // Añade esta línea para permitir contenido mixto (aunque no debería ser necesaria para localhost)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                Log.d("WebView", "Página HTML cargada: $url")
                                // Intentar cargar el modelo usando la URL del servidor local
                                val modelFileName = farm.modelFileName // Usa el campo String
                                val modelUrl = "${assetServer.getBaseUrl()}/models/$modelFileName.glb" // ✅ URL del servidor local
                                Log.d("WebView", "Intentando cargar modelo desde: $modelUrl")

                                // Llamamos a la función JavaScript para cargar el modelo
                                view?.evaluateJavascript("loadModelAtPath('$modelUrl')", null)
                            }

                            // ✅ Versión compatible para onReceivedError
                            override fun onReceivedError(
                                view: WebView?,
                                errorCode: Int,
                                description: String?,
                                failingUrl: String?
                            ) {
                                super.onReceivedError(view, errorCode, description, failingUrl)
                                Log.e("WebView", "Error en WebView: $errorCode - $description en URL: $failingUrl")
                            }
                        }
                        loadUrl("file:///android_asset/model_3d_viewer.html")
                    }
                },
                update = { webView ->
                    // Aquí puedes aplicar la rotación si lo manejas manualmente en JS
                    // webView.evaluateJavascript("javascript:setRotation(${rotation})", null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para tutorial
            Button(
                onClick = {
                    // Aquí puedes abrir el tutorial en navegador
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Tutorial")
            }
        }
    }
}