# Sistema de Autenticación - MineFarms

## 📋 Descripción General

Sistema completo de autenticación local con Room Database que permite a los usuarios:
- ✅ Registrarse con usuario, email y contraseña
- ✅ Iniciar sesión con credenciales guardadas localmente
- ✅ Personalizar perfil con nombre para mostrar y avatar emoji
- ✅ Mantener sesión activa (persistencia con DataStore)
- ✅ Cerrar sesión de forma segura

## 🗄️ Arquitectura de Base de Datos

### Tablas Creadas

#### 1. **users** (UserEntity.kt)
Almacena la información de los usuarios registrados.

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,          // Único, minúsculas, sin espacios
    val email: String,             // Único
    val passwordHash: String,      // SHA-256 hash
    val displayName: String,       // Nombre para mostrar
    val createdAt: Long,          // Timestamp de creación
    val avatarEmoji: String       // Emoji del avatar (🌾, ⛏️, etc.)
)
```

**Índices:**
- `username` (UNIQUE)
- `email` (UNIQUE)

#### 2. **favorites** (FavoriteEntity.kt)
Relación muchos-a-muchos entre usuarios y granjas favoritas.

```kotlin
@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "farmId"]
)
data class FavoriteEntity(
    val userId: Int,      // FK a users.id
    val farmId: Int,      // ID de la granja (de FarmRepository)
    val addedAt: Long     // Timestamp cuando se añadió
)
```

#### 3. **user_farms** (UserFarmEntity.kt)
Granjas personalizadas creadas por usuarios.

```kotlin
@Entity(tableName = "user_farms")
data class UserFarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,              // FK a users.id
    val name: String,
    val description: String,
    val materials: String,        // JSON: ["Material1", "Material2"]
    val difficulty: String,       // "Fácil", "Medio", "Difícil", "Experto"
    val production: String,
    val productionRate: String,
    val process: String,
    val tutorialUrl: String?,
    val imageUri: String?,        // URI local de la imagen
    val tags: String,             // JSON: ["tag1", "tag2"]
    val createdAt: Long,
    val isPublic: Boolean         // Si otros usuarios pueden verla
)
```

## 🔐 Seguridad

### Hash de Contraseñas
- **Algoritmo:** SHA-256
- **Implementación:** `MessageDigest.getInstance("SHA-256")`
- **Nota:** Para producción real, se recomienda usar BCrypt o Argon2

```kotlin
private fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
```

### Validaciones

#### Registro:
- ✅ Username único (case-insensitive)
- ✅ Email único
- ✅ Username sin espacios ni mayúsculas
- ✅ Contraseña mínimo 6 caracteres
- ✅ Confirmación de contraseña coincidente

#### Login:
- ✅ Verificación de username + passwordHash
- ✅ Mensajes de error claros sin revelar información específica

## 📱 Componentes del Sistema

### 1. Data Layer

#### `AppDatabase.kt`
Base de datos Room principal con Singleton pattern.

```kotlin
@Database(
    entities = [UserEntity::class, FavoriteEntity::class, UserFarmEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun userFarmDao(): UserFarmDao
}
```

#### `UserDao.kt`
Operaciones CRUD para usuarios:
- `login(username, passwordHash)`: Buscar usuario por credenciales
- `getUserById(id)`: Obtener usuario por ID (Flow)
- `insert(user)`: Registrar nuevo usuario
- `usernameExists(username)`: Verificar unicidad
- `emailExists(email)`: Verificar unicidad

#### `FavoriteDao.kt`
Gestión de favoritos:
- `addFavorite(favorite)`: Añadir granja a favoritos
- `removeFavorite(userId, farmId)`: Quitar de favoritos
- `isFavorite(userId, farmId)`: Verificar si es favorito (Flow)
- `getFavoriteFarmIds(userId)`: Lista de IDs favoritos (Flow)

#### `UserFarmDao.kt`
Gestión de granjas personalizadas:
- `insert(farm)`: Crear granja personalizada
- `getUserFarms(userId)`: Granjas del usuario (Flow)
- `getPublicFarms()`: Granjas públicas de todos (Flow)
- `delete(farm)`: Eliminar granja

### 2. Business Logic

#### `UserManager.kt`
Gestión de sesión con DataStore Preferences.

```kotlin
class UserManager(context: Context) {
    suspend fun saveUserId(userId: Int)      // Guardar sesión
    suspend fun clearUserId()                 // Cerrar sesión
    fun currentUserId(): Flow<Int?>           // Observar sesión activa
}
```

#### `AuthRepository.kt`
Lógica de negocio de autenticación.

**Métodos:**
- `login(username, password): Result<Unit>`
- `register(username, email, password, displayName, avatarEmoji): Result<Unit>`
- `logout()`
- `getCurrentUserId(): Flow<Int?>`
- `getUserById(id): Flow<UserEntity?>`

### 3. Presentation Layer

#### `AuthViewModel.kt`
ViewModel que expone el estado de autenticación.

```kotlin
data class AuthState(
    val isLoggedIn: Boolean = false,
    val userId: Int? = null,
    val username: String? = null,
    val displayName: String? = null,
    val avatarEmoji: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
```

**Métodos:**
- `login(username, password, onResult)`
- `register(..., onResult)`
- `logout()`
- `clearError()`

#### `LoginScreen.kt`
Pantalla de inicio de sesión con diseño Minecraft.

**Características:**
- 🎨 Gradiente de fondo (primaryContainer → background)
- 🏗️ Card elevado con formulario
- 👤 Campos: username, password
- 👁️ Toggle de visibilidad de contraseña
- ⚠️ Mensajes de error animados
- 🚀 Botón con CircularProgressIndicator durante carga
- 🔗 Botón para ir a registro
- 🚪 Opción "Continuar sin iniciar sesión"

#### `RegisterScreen.kt`
Pantalla de registro completa.

**Características:**
- 😀 Selector de avatar emoji (24 opciones)
- 📝 Campos: displayName, username, email, password, confirmPassword
- ✅ Validación en tiempo real
- 🎯 Auto-formato de username (lowercase, sin espacios)
- 📧 Teclado específico para email
- 🔐 Indicador de fortaleza de contraseña
- ❌ Verificación de contraseñas coincidentes

### 4. Navigation

#### `Navigation.kt` (Actualizado)
Gestión de rutas con integración de auth.

**Rutas:**
- `"login"` → LoginScreen (inicio)
- `"register"` → RegisterScreen
- `"farmList"` → FarmListScreen (requiere login)
- `"farmDetail/{farmId}"` → FarmDetailScreen

**Flujo:**
1. App inicia en `"login"`
2. Usuario puede registrarse o loguearse
3. Al éxito → navega a `"farmList"` (popUpTo login)
4. Usuario puede explorar granjas

## 🎨 Diseño UI

### Paleta de Colores Minecraft (Heredada)
- **Verde Grass:** primary
- **Gris Stone:** secondary
- **Café Dirt:** tertiary
- **Cyan Diamond:** accentos especiales
- **Rojo Redstone:** errores
- **Oro:** detalles premium

### Elementos Visuales
- **Animaciones:** fadeIn + slideInVertically (500ms)
- **Cards:** RoundedCornerShape(20.dp), elevation 8.dp
- **Inputs:** RoundedCornerShape(12.dp)
- **Botones:** height 56.dp, con iconos
- **Logo:** Surface circular 100.dp con emojis ⛏️🌾

## 🚀 Flujo de Usuario

### Registro de Nuevo Usuario

1. Usuario abre la app → ve LoginScreen
2. Click en "Crear Cuenta Nueva" → RegisterScreen
3. Completa formulario:
   - Elige avatar emoji
   - Ingresa nombre para mostrar
   - Crea username (auto-lowercase)
   - Ingresa email
   - Crea contraseña (min 6 chars)
   - Confirma contraseña
4. Click "Crear Cuenta"
5. `AuthViewModel.register()` → `AuthRepository.register()`:
   - Verifica username único
   - Verifica email único
   - Hashea contraseña con SHA-256
   - Inserta en tabla `users`
   - Guarda userId en DataStore
6. Navega a `farmList`

### Inicio de Sesión

1. Usuario en LoginScreen
2. Ingresa username y password
3. Click "Iniciar Sesión"
4. `AuthViewModel.login()` → `AuthRepository.login()`:
   - Hashea password ingresado
   - Busca en DB: `SELECT * WHERE username = ? AND passwordHash = ?`
   - Si encuentra → guarda userId en DataStore
   - Si no encuentra → error "Credenciales incorrectas"
5. Si éxito → navega a `farmList`

### Sesión Persistente

```kotlin
// En AuthViewModel.init {}
authRepository.getCurrentUserId().collect { userId ->
    if (userId != null) {
        // Usuario tiene sesión activa
        loadUserData(userId)
    }
}
```

- Al abrir la app, `AuthViewModel` verifica DataStore
- Si hay `userId` guardado → carga datos del usuario
- Permite implementar auto-login en el futuro

### Cierre de Sesión

```kotlin
authViewModel.logout()
// 1. Llama a authRepository.logout()
// 2. userManager.clearUserId()
// 3. authState se resetea
// 4. Navega de vuelta a login
```

## 📦 Dependencias Agregadas

```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// DataStore (Preferences)
implementation("androidx.datastore:datastore-preferences:1.0.0")

// Coil (para cargar imágenes)
implementation("io.coil-kt:coil-compose:2.5.0")
```

## 🔮 Próximos Pasos

### Funcionalidades Pendientes:

1. **Sistema de Favoritos**
   - [ ] Botón de favorito en FarmDetailScreen
   - [ ] Pantalla de granjas favoritas
   - [ ] Integración con FavoriteDao

2. **Subir Granjas Personalizadas**
   - [ ] Pantalla UploadFarmScreen
   - [ ] Image picker (ActivityResultContracts)
   - [ ] Formulario completo con validaciones
   - [ ] Guardar en `user_farms` table

3. **Perfil de Usuario**
   - [ ] ProfileScreen con datos del usuario
   - [ ] Botón de logout
   - [ ] Lista de granjas subidas
   - [ ] Lista de favoritos

4. **Explorar Granjas Comunitarias**
   - [ ] Tab en FarmListScreen para granjas públicas
   - [ ] Filtrado por tags
   - [ ] Búsqueda por nombre

5. **Mejoras Futuras**
   - [ ] Editar perfil
   - [ ] Cambiar contraseña
   - [ ] Recuperación de cuenta
   - [ ] Avatar con imágenes (no solo emojis)
   - [ ] Sistema de valoraciones
   - [ ] Comentarios en granjas

## 🐛 Solución de Problemas

### Error: "Username already exists"
- El username debe ser único
- Se convierte a lowercase automáticamente
- Intenta con otro username

### Error: "Email already registered"
- El email ya está en uso
- Usa otro email o recupera tu cuenta

### Error: "Invalid credentials"
- Username o contraseña incorrectos
- Verifica que no haya espacios extra
- Username es case-insensitive

### La sesión no persiste
- Verifica que DataStore esté configurado correctamente
- Revisa que `UserManager` esté guardando el userId
- Comprueba los logs de `AuthViewModel.checkCurrentUser()`

## 📝 Notas Técnicas

### ¿Por qué SHA-256 y no BCrypt?
SHA-256 es más simple para un proyecto de demostración local. Para producción:
```kotlin
// Cambiar a BCrypt:
implementation("org.mindrot:jbcrypt:0.4")

fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}

fun checkPassword(password: String, hash: String): Boolean {
    return BCrypt.checkpw(password, hash)
}
```

### ¿Por qué JSON para listas en UserFarmEntity?
Room no soporta listas directamente. Alternativas:
1. **JSON String** (actual) - Simple, funciona bien para listas pequeñas
2. **Type Converters** - Más elegante, requiere configuración adicional
3. **Tablas separadas** - Normalizado, más complejo

### Manejo de Errores
Todos los métodos de `AuthRepository` retornan `Result<T>`:
```kotlin
result.fold(
    onSuccess = { /* Éxito */ },
    onFailure = { error -> /* error.message */ }
)
```

## 🎯 Conclusión

Sistema completo de autenticación implementado con:
- ✅ Room Database (3 tablas)
- ✅ DataStore para sesiones
- ✅ MVVM arquitectura
- ✅ UI hermosa con tema Minecraft
- ✅ Validaciones completas
- ✅ Seguridad básica (hash de contraseñas)

El sistema está listo para expandirse con favoritos y subida de granjas personalizadas.
