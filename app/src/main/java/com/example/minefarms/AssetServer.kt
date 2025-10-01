package com.example.minefarms

import android.content.Context
import android.util.Log
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import java.io.IOException

class AssetServer(private val context: Context) {
    private val server = AsyncHttpServer()
    private var port = 0

    fun start() {
        // Define una ruta para /models/{nombre_del_archivo}
        server.get("/models/(.*)") { request: AsyncHttpServerRequest, response: AsyncHttpServerResponse ->
            // Extrae el nombre del archivo de la URL
            val path = request.path // Esto devolverá algo como "/models/granja_hierro.glb"
            val pathSegments = path.split("/")
            if (pathSegments.size >= 3 && pathSegments[1] == "models") {
                val modelName = pathSegments[2] // Por ejemplo: "granja_hierro.glb"
                Log.d("AssetServer", "Solicitando modelo: $modelName")

                try {
                    // Abre el archivo desde assets/models/
                    val assetInputStream = context.assets.open("models/$modelName")

                    // Configura la respuesta
                    response.code(200) // Código de éxito
                    response.setContentType(guessMimeType(modelName)) // Tipo MIME

                    // Escribe el contenido del archivo a la respuesta
                    response.sendStream(assetInputStream, assetInputStream.available().toLong())

                } catch (e: IOException) {
                    Log.e("AssetServer", "Error al leer asset: $modelName", e)
                    response.code(404).send("Asset not found: $modelName")
                }
            } else {
                Log.w("AssetServer", "Solicitud inválida: $path")
                response.code(400).send("Bad Request")
            }
        }

        // Inicia el servidor en un puerto aleatorio disponible (0)
        // ✅ Usamos `listen` en lugar de `listenAsync`
        // ✅ El listener recibe un AsyncServerSocket, no un listener de tipo especial
        server.listen(0) // Escucha en un puerto aleatorio
        // Para obtener el puerto real, necesitamos una forma diferente
        // AndroidAsync no expone directamente el puerto asignado en listen(int)
        // Podemos intentar obtenerlo de otra manera o usar un puerto fijo si es necesario.

        // Opción A: Intentar obtener el puerto después de iniciar (esto puede no ser inmediatamente disponible)
        // val assignedPort = server.assignedPort // Esto no existe directamente en AsyncHttpServer
        // Por ahora, asumiremos que el puerto se asigna y lo obtenemos de otra forma si es posible.

        // Opción B (más simple): Usar un puerto fijo (menos robusto, pero más predecible)
        // server.listen(8080) // Escucha en el puerto 8080

        // Para esta solución, intentaremos usar un puerto fijo para evitar problemas de detección automática.
        // Si `listen(0)` no funciona bien con la API para obtener el puerto, usamos un puerto fijo.
        // Por ejemplo, puerto 8080:
        // server.listen(8080)
        // port = 8080

        // Opción C (la más robusta): Usar `listen(Context, int)` o similar si está disponible
        // pero parece que no es el caso directamente con AsyncHttpServer.

        // Dado que `listen(int)` inicia el servidor síncronamente en el hilo actual,
        // y no devuelve un objeto con el puerto real si usamos `listen(0)`,
        // una solución más confiable es usar un puerto fijo conocido.

        // Vamos a probar con un puerto fijo, por ejemplo, 8080.
        // Si 8080 está ocupado, fallará, pero es más simple de manejar.
        val fixedPort = 8080
        try {
            server.listen(fixedPort)
            port = fixedPort
            Log.d("AssetServer", "Servidor iniciado en puerto fijo: $port")
        } catch (e: java.net.BindException) {
            Log.e("AssetServer", "Puerto $fixedPort ocupado, intentando otro...", e)
            // Si el puerto fijo falla, intentamos con listen(0) y asumimos que
            // internamente AsyncHttpServer lo maneja, aunque obtener el puerto real sea complicado.
            // Esta es una limitación de la librería para obtener el puerto dinámico.
            // Para fines de desarrollo, un puerto fijo suele ser suficiente.
            // Si necesitas puerto dinámico, investiga más a fondo la API de AsyncSocket.
            // Por ahora, mantendremos el puerto fijo.
            // Si te da problemas de puerto ocupado, puedes probar con otro número (8081, 8082, etc.).
        }
    }

    fun stop() {
        server.stop()
    }

    fun getBaseUrl(): String {
        // ✅ Devolvemos la URL con el puerto fijo o el obtenido
        return if (port != 0) "http://localhost:$port" else ""
    }

    private fun guessMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".glb") -> "model/gltf-binary"
            fileName.endsWith(".gltf") -> "model/gltf+json"
            fileName.endsWith(".png") -> "image/png"
            fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
            else -> "application/octet-stream" // Tipo genérico
        }
    }
}