package com.example.minefarms.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto singleton para manejar la configuración de Retrofit
 * Proporciona una instancia única del servicio de API
 */
object RetrofitClient {
    
    // URL base de la API de Minecraft (usando minecraft-ids.grahamedgecombe.com como ejemplo)
    // Puedes cambiar esto a otra API de Minecraft si lo prefieres
    private const val BASE_URL = "https://minecraft-ids.grahamedgecombe.com/api/"
    
    /**
     * Configuración de logging para debugging
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    /**
     * Cliente HTTP con configuración de timeout y logging
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Instancia de Retrofit configurada
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    /**
     * Instancia del servicio de API
     */
    val minecraftApiService: MinecraftApiService by lazy {
        retrofit.create(MinecraftApiService::class.java)
    }
}
