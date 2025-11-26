plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    // ❌ Eliminamos esta línea
    // alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.minefarms"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.minefarms"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuración para firma de APK
    signingConfigs {
        create("release") {
            // Para producción, estas credenciales deben estar en gradle.properties
            // o variables de entorno, no hardcoded aquí
            storeFile = file("../minefarms.keystore") // Si tienes keystore
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "minefarms123"
            keyAlias = System.getenv("KEY_ALIAS") ?: "minefarms"
            keyPassword = System.getenv("KEY_PASSWORD") ?: "minefarms123"
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Comentar la siguiente línea si no tienes keystore configurado
            // signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true // ✅ Esto habilita Compose
    }

    // ✅ Definimos la versión del compilador de Compose aquí
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() // ✅ Corregido
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // ViewModel para Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Navigation Component para Jetpack Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Material Icons Extended para más iconos
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Room Database para almacenamiento local
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // DataStore para preferencias
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Coil para cargar imágenes
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Retrofit para consumo de API REST
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Gson para JSON
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}