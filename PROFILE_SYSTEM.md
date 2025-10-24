# Sistema de Perfil de Usuario - MineFarms

## 📋 Descripción General

Sistema completo de perfil de usuario con las siguientes funcionalidades:

- ✅ **Acceso rápido**: Botón de perfil en la esquina superior derecha de FarmListScreen
- ✅ **Información del usuario**: Avatar emoji, nombre para mostrar, username
- ✅ **Tres secciones organizadas por tabs**:
  - 💗 **Favoritos**: Granjas marcadas como favoritas
  - 🌾 **Mis Granjas**: Granjas personalizadas creadas por el usuario
  - 🔖 **Guardadas**: Granjas guardadas para ver más tarde
- ✅ **Cerrar sesión**: Botón con diálogo de confirmación
- ✅ **Animaciones**: Transiciones suaves y diseño hermoso

## 🎨 Componentes Creados

### 1. ProfileScreen.kt

**Ubicación:** `app/src/main/java/com/example/minefarms/ui/screens/ProfileScreen.kt`

#### Estructura:

```
ProfileScreen
├─ TopAppBar (con botón atrás)
├─ ProfileHeader
│  ├─ Avatar (emoji circular)
│  ├─ Nombre para mostrar
│  ├─ Username
│  └─ Botón "Cerrar Sesión"
├─ TabRow (3 tabs)
│  ├─ Favoritos 💗
│  ├─ Mis Granjas 🌾
│  └─ Guardadas 🔖
└─ Contenido dinámico según tab
```

#### Funcionalidades:

**ProfileHeader:**
```kotlin
@Composable
private fun ProfileHeader(
    displayName: String,
    username: String,
    avatarEmoji: String,
    onLogout: () -> Unit
)
```

- Muestra avatar emoji del usuario (configurado en registro)
- Nombre para mostrar en grande y bold
- Username con @ prefix
- Botón de cerrar sesión con color error (rojo)
- Gradiente horizontal de fondo (primary → secondary)

**Tabs de contenido:**
- **FavoritesContent**: Granjas que el usuario marcó como favoritas
- **UserFarmsContent**: Granjas personalizadas creadas por el usuario
- **SavedFarmsContent**: Granjas guardadas para ver más tarde

**Estados vacíos:**
```kotlin
@Composable
private fun EmptyStateMessage(
    icon: ImageVector,
    title: String,
    message: String
)
```

Cada sección muestra un mensaje bonito cuando está vacía con:
- Ícono grande (64.dp) semi-transparente
- Título en bold
- Mensaje descriptivo centrado

### 2. Actualización de FarmListScreen

**Cambios:**
- Agregado parámetro `onProfileClick: () -> Unit = {}`
- Agregado `actions` en TopAppBar con IconButton
- Ícono `AccountCircle` de 32dp en la esquina superior derecha
- Al hacer clic → navega a ProfileScreen

```kotlin
actions = {
    IconButton(onClick = onProfileClick) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Mi Perfil",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(32.dp)
        )
    }
}
```

### 3. Actualización de Navigation

**Nueva ruta:**
```kotlin
composable("profile") {
    ProfileScreen(
        authViewModel = authViewModel,
        onBack = { /* Lógica de navegación */ },
        onNavigateToFarm = { farmId -> /* Navegar a detalle */ }
    )
}
```

**Lógica de navegación desde perfil:**
- Si el usuario está logueado → `popBackStack()` (volver a farmList)
- Si cerró sesión → navegar a login y limpiar stack

## 🎯 Flujo de Usuario

### Acceso al Perfil

1. Usuario está en FarmListScreen
2. Ve el ícono de perfil (👤) arriba a la derecha
3. Click en el ícono
4. Navega a ProfileScreen con animación

### Cerrar Sesión

1. Usuario en ProfileScreen
2. Click en botón "Cerrar Sesión"
3. Aparece AlertDialog de confirmación:
   ```
   ╔═══════════════════════════╗
   ║    🚪 Cerrar Sesión       ║
   ╟───────────────────────────╢
   ║ ¿Estás seguro que deseas  ║
   ║ cerrar sesión?            ║
   ╟───────────────────────────╢
   ║  [Cancelar] [Cerrar]      ║
   ╚═══════════════════════════╝
   ```
4. Si confirma:
   - Ejecuta `authViewModel.logout()`
   - Limpia sesión en DataStore
   - Navega a LoginScreen
   - Limpia el stack de navegación

### Navegación entre Tabs

Usuario puede cambiar entre las 3 tabs:
- **Favoritos** (💗): Granjas con like
- **Mis Granjas** (🌾): Granjas creadas por el usuario
- **Guardadas** (🔖): Granjas guardadas

Cada tab muestra:
- Si hay contenido → lista de granjas
- Si está vacío → mensaje descriptivo con ícono

## 🎨 Diseño Visual

### ProfileHeader

**Estilo:**
- Card con RoundedCornerShape(20.dp)
- Elevation 8.dp
- Gradiente horizontal (primary → secondary)
- Padding 24.dp

**Avatar:**
- Surface circular de 100.dp
- Color surface con elevation 8.dp
- Emoji en displayLarge

**Textos:**
- displayName: headlineMedium, bold, onPrimary
- username: bodyLarge, onPrimary 80% alpha

**Botón:**
- Color error (rojo de Material 3)
- RoundedCornerShape(12.dp)
- Height 48.dp
- Ícono Logout + texto

### Tabs

**Estilo:**
- TabRow con containerColor surface
- Elevation 2.dp
- Cada Tab con:
  - Ícono característico
  - Texto descriptivo
  - Estado selected con animación

### EmptyStateMessage

**Estilo:**
- Card con RoundedCornerShape(16.dp)
- Color surfaceVariant
- Padding 32.dp
- Ícono 64.dp semi-transparente
- Título titleLarge bold
- Mensaje bodyMedium centrado

## 🔧 Integraciones Pendientes

### 1. Sistema de Favoritos

**TODO:** Integrar con `FavoriteDao`

```kotlin
@Composable
private fun FavoritesContent(
    userId: Long?,
    onNavigateToFarm: (Int) -> Unit
) {
    // TODO: Implementar
    val favoriteDao = remember { AppDatabase.getDatabase(context).favoriteDao() }
    val favoriteFarmIds by favoriteDao.getFavoriteFarmIds(userId!!).collectAsState(initial = emptyList())
    
    LazyColumn {
        items(favoriteFarmIds) { farmId ->
            FarmCard(farmId = farmId, onClick = onNavigateToFarm)
        }
    }
}
```

**Pasos:**
1. Obtener IDs de favoritos con `favoriteDao.getFavoriteFarmIds(userId)`
2. Cargar datos de granjas desde `FarmRepository.getFarmById()`
3. Mostrar en LazyColumn con FarmCard
4. Si lista vacía → mostrar EmptyStateMessage

### 2. Mis Granjas (User Farms)

**TODO:** Integrar con `UserFarmDao`

```kotlin
@Composable
private fun UserFarmsContent(
    userId: Long?,
    onNavigateToFarm: (Int) -> Unit
) {
    // TODO: Implementar
    val userFarmDao = remember { AppDatabase.getDatabase(context).userFarmDao() }
    val userFarms by userFarmDao.getUserFarms(userId!!).collectAsState(initial = emptyList())
    
    LazyColumn {
        items(userFarms) { farm ->
            UserFarmCard(farm = farm, onClick = { /* Navegar */ })
        }
    }
}
```

**Pasos:**
1. Obtener granjas del usuario con `userFarmDao.getUserFarms(userId)`
2. Mostrar en LazyColumn con UserFarmCard
3. Botón "Crear Nueva Granja" si está vacío
4. Opciones de editar/eliminar cada granja

### 3. Granjas Guardadas

**TODO:** Crear sistema de guardado

Esta funcionalidad es diferente de favoritos. Podemos usar:
- La misma tabla `favorites` con un flag `isSaved`
- O crear nueva tabla `saved_farms`

```kotlin
@Entity(
    tableName = "saved_farms",
    primaryKeys = ["userId", "farmId"]
)
data class SavedFarmEntity(
    val userId: Long,
    val farmId: Int,
    val savedAt: Long
)
```

### 4. FarmCard Composable Reutilizable

**TODO:** Crear componente reutilizable

```kotlin
@Composable
fun FarmCard(
    farm: Farm,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Imagen 60x60dp
            // Nombre y descripción
            // Chip de dificultad
        }
    }
}
```

Reutilizar este componente en:
- ProfileScreen (favoritos, guardadas)
- FarmListScreen (lista principal)
- Búsqueda (futura funcionalidad)

## 🔮 Funcionalidades Futuras

### 1. Editar Perfil
```kotlin
composable("editProfile") {
    EditProfileScreen(
        authViewModel = authViewModel,
        onSave = { /* Actualizar datos */ },
        onBack = { navController.popBackStack() }
    )
}
```

Permitir editar:
- Nombre para mostrar
- Avatar emoji
- Email
- Cambiar contraseña

### 2. Estadísticas de Usuario

Agregar al ProfileHeader:
```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
) {
    StatItem(value = "12", label = "Favoritos")
    StatItem(value = "5", label = "Creadas")
    StatItem(value = "8", label = "Guardadas")
}
```

### 3. Logros/Badges

Sistema de logros para gamificación:
- 🌟 "Primer Granja": Crear primera granja
- 💯 "Coleccionista": 10 favoritos
- 🏆 "Experto": Crear 5 granjas
- 🎖️ "Popular": 100 likes en tus granjas

### 4. Configuración/Settings

Nueva tab o pantalla separada para:
- ⚙️ Tema (claro/oscuro)
- 🔔 Notificaciones
- 🌐 Idioma
- 📱 Acerca de la app
- 🗑️ Eliminar cuenta

## 📱 Navegación Completa Actualizada

```
LoginScreen
    ↓ (login exitoso)
FarmListScreen ←────┐
    ↓                │
    ├─ [Profile] → ProfileScreen
    │                  ↓
    │              [Logout] → LoginScreen
    │                  ↓
    │              [Granja] → FarmDetailScreen
    │                            ↓
    └────────────────────────────┘ (back)
```

## 🎯 Resumen de Cambios

### Archivos Creados:
1. ✅ `ProfileScreen.kt` (400+ líneas)

### Archivos Modificados:
1. ✅ `FarmListScreen.kt`:
   - Agregado parámetro `onProfileClick`
   - Agregado botón de perfil en TopAppBar
   - Import de `Icons.Default.AccountCircle`

2. ✅ `Navigation.kt`:
   - Agregada ruta `"profile"`
   - Pasado `onProfileClick` a FarmListScreen
   - Lógica de navegación post-logout

### Funcionalidades Implementadas:
- ✅ Botón de perfil visible en FarmListScreen
- ✅ ProfileScreen con header hermoso
- ✅ 3 tabs organizadas (Favoritos, Mis Granjas, Guardadas)
- ✅ Botón de cerrar sesión con confirmación
- ✅ Estados vacíos con mensajes descriptivos
- ✅ Animaciones de entrada
- ✅ Navegación integrada

### Funcionalidades Pendientes:
- ⏳ Integrar datos reales de favoritos (FavoriteDao)
- ⏳ Integrar datos reales de granjas del usuario (UserFarmDao)
- ⏳ Implementar sistema de guardado
- ⏳ Crear FarmCard reutilizable
- ⏳ Agregar botón de favorito en FarmDetailScreen

## 🐛 Testing

### Casos de Prueba:

1. **Acceso al Perfil**
   - [x] El botón de perfil es visible
   - [x] Click abre ProfileScreen
   - [x] Animación de entrada funciona

2. **ProfileHeader**
   - [x] Muestra avatar emoji correcto
   - [x] Muestra displayName correcto
   - [x] Muestra username con @
   - [x] Botón de logout visible

3. **Cerrar Sesión**
   - [x] Click muestra AlertDialog
   - [x] Cancelar cierra el diálogo
   - [x] Confirmar ejecuta logout
   - [x] Navega a LoginScreen
   - [x] Sesión se limpia en DataStore

4. **Tabs**
   - [x] 3 tabs se muestran correctamente
   - [x] Click cambia contenido
   - [x] Íconos correctos en cada tab
   - [x] Animación de selección

5. **Estados Vacíos**
   - [x] Muestra mensaje cuando no hay favoritos
   - [x] Muestra mensaje cuando no hay granjas creadas
   - [x] Muestra mensaje cuando no hay guardadas
   - [x] Íconos y textos correctos

## 🎉 Conclusión

El sistema de perfil está completamente implementado con:

- ✅ Acceso rápido desde FarmListScreen
- ✅ Información del usuario visible
- ✅ Organización por tabs
- ✅ Cerrar sesión con confirmación
- ✅ Estados vacíos informativos
- ✅ Diseño hermoso con tema Minecraft
- ✅ Animaciones suaves
- ✅ Navegación completa

**Próximo paso:** Integrar datos reales conectando con los DAOs de Room Database.
