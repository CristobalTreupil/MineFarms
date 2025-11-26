package com.example.minefarms.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.data.entity.UserFarmEntity
import com.example.minefarms.model.Farm
import com.example.minefarms.repository.FarmRepository
import com.example.minefarms.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onNavigateToFarm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by authViewModel.authState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    // Diálogo de confirmación de logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text("Cerrar Sesión")
            },
            text = {
                Text("¿Estás seguro que deseas cerrar sesión?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.logout()
                        showLogoutDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
                // Header del perfil
                ProfileHeader(
                    displayName = authState.displayName ?: "Usuario",
                    username = authState.username ?: "",
                    avatarEmoji = authState.avatarEmoji ?: "👤",
                    onLogout = { showLogoutDialog = true }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tabs de contenido
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                            text = { Text("Favoritos") }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            icon = { Icon(Icons.Default.Upload, contentDescription = null) },
                            text = { Text("Mis Granjas") }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            icon = { Icon(Icons.Default.Bookmark, contentDescription = null) },
                            text = { Text("Guardadas") }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Contenido según la tab seleccionada
                when (selectedTab) {
                    0 -> FavoritesContent(
                        userId = authState.userId,
                        onNavigateToFarm = onNavigateToFarm
                    )
                    1 -> UserFarmsContent(
                        userId = authState.userId,
                        onNavigateToFarm = onNavigateToFarm
                    )
                    2 -> SavedFarmsContent(
                        userId = authState.userId,
                        onNavigateToFarm = onNavigateToFarm
                    )
                }
                
                Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    displayName: String,
    username: String,
    avatarEmoji: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarEmoji,
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nombre para mostrar
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            
            // Username
            Text(
                text = "@$username",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón de cerrar sesión
            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun FavoritesContent(
    userId: Long?,
    onNavigateToFarm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val likedFarmEntities by database.favoriteDao().getLikedFarms(userId ?: 0L).collectAsState(initial = emptyList())
    val farmRepository = remember { FarmRepository(database.farmDao()) }
    val allFarms by farmRepository.getAllFarms().collectAsState(initial = emptyList())
    
    // Filtrar granjas que están en favoritos
    val likedFarms = remember(allFarms, likedFarmEntities) {
        allFarms.filter { farm -> 
            likedFarmEntities.any { it.farmId == farm.id }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (userId == null) {
            EmptyStateMessage(
                icon = Icons.Default.Favorite,
                title = "Inicia sesión",
                message = "Debes iniciar sesión para ver tus granjas favoritas"
            )
        } else if (likedFarms.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.Default.Favorite,
                title = "Sin favoritos",
                message = "Aún no has marcado ninguna granja como favorita.\n\nExplora las granjas y marca las que más te gusten."
            )
        } else {
            likedFarms.forEach { farm ->
                FarmCardItem(
                    farm = farm,
                    onClick = { onNavigateToFarm(farm.id.toInt()) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun UserFarmsContent(
    userId: Long?,
    onNavigateToFarm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val userFarms by database.userFarmDao().getUserFarms(userId ?: 0L).collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    val farmRepository = remember { FarmRepository(database.farmDao()) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (userId == null) {
            EmptyStateMessage(
                icon = Icons.Default.Upload,
                title = "Inicia sesión",
                message = "Debes iniciar sesión para ver tus granjas creadas"
            )
        } else if (userFarms.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.Default.Upload,
                title = "Sin granjas creadas",
                message = "Aún no has creado ninguna granja personalizada.\n\n¡Sé el primero en compartir tu diseño!"
            )
        } else {
            userFarms.forEach { userFarm ->
                UserFarmCard(
                    userFarm = userFarm,
                    onClick = { 
                        // Convertir UserFarm a Farm y guardar en tabla farms para poder navegarlo
                        scope.launch {
                            val farm = Farm(
                                id = 0, // Auto-generado por Room
                                name = userFarm.name,
                                description = userFarm.description,
                                materials = userFarm.materials.split(",").map { it.trim() }.filter { it.isNotBlank() },
                                difficulty = userFarm.difficulty,
                                production = userFarm.production,
                                productionRate = "Variable",
                                process = "Granja personalizada creada por usuario",
                                tutorialUrl = userFarm.tutorialUrl ?: "",
                                imageResourceName = "user_farm_${userFarm.id}",
                                tags = listOf("Usuario", "Personalizada"),
                                imageUri = userFarm.imageUri // Pasar el URI real de la imagen
                            )
                            val farmId = farmRepository.insertFarm(farm)
                            onNavigateToFarm(farmId.toInt())
                        }
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SavedFarmsContent(
    userId: Long?,
    onNavigateToFarm: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val savedFarmEntities by database.favoriteDao().getSavedFarms(userId ?: 0L).collectAsState(initial = emptyList())
    val farmRepository = remember { FarmRepository(database.farmDao()) }
    val allFarms by farmRepository.getAllFarms().collectAsState(initial = emptyList())
    
    // Filtrar granjas que están guardadas
    val savedFarms = remember(allFarms, savedFarmEntities) {
        allFarms.filter { farm -> 
            savedFarmEntities.any { it.farmId == farm.id }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (userId == null) {
            EmptyStateMessage(
                icon = Icons.Default.Bookmark,
                title = "Inicia sesión",
                message = "Debes iniciar sesión para ver tus granjas guardadas"
            )
        } else if (savedFarms.isEmpty()) {
            EmptyStateMessage(
                icon = Icons.Default.Bookmark,
                title = "Sin granjas guardadas",
                message = "Aún no has guardado ninguna granja para ver más tarde.\n\nUsa el botón de guardar en cualquier granja."
            )
        } else {
            savedFarms.forEach { farm ->
                FarmCardItem(
                    farm = farm,
                    onClick = { onNavigateToFarm(farm.id.toInt()) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
} 

@Composable
private fun EmptyStateMessage(
    icon: ImageVector,
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun FarmCardItem(
    farm: Farm,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji de la granja
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "🌾",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            
            // Información de la granja
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = farm.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = farm.difficulty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun UserFarmCard(
    userFarm: UserFarmEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la granja
            if (userFarm.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(userFarm.imageUri)),
                    contentDescription = userFarm.name,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🌾",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
            
            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = userFarm.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userFarm.difficulty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (userFarm.production.isNotBlank()) {
                    Text(
                        text = "Produce: ${userFarm.production}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
