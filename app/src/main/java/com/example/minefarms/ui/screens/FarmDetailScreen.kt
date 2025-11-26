package com.example.minefarms.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.data.entity.FavoriteEntity
import com.example.minefarms.repository.FarmRepository
import com.example.minefarms.ui.viewmodel.AuthViewModel
import com.example.minefarms.utils.ImageUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(
    farmId: Long,
    farmViewModel: com.example.minefarms.viewmodel.FarmViewModel,
    onBack: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val userId = authState.userId
    
    // Obtener la granja desde el ViewModel - usar remember para evitar llamadas repetidas
    val farmFlow = remember(farmId) { farmViewModel.getFarmById(farmId) }
    val farm by farmFlow.collectAsState()
    
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val favoriteDao = remember { database.favoriteDao() }
    
    // Estado derivado de favoritos - usar remember para evitar recrear el Flow
    val favoritesFlow = remember(userId) {
        if (userId != null) {
            favoriteDao.getFavoritesByUser(userId)
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }
    val favorites by favoritesFlow.collectAsState(initial = emptyList())
    
    val currentFavorite = remember(favorites, farmId) {
        favorites.find { it.farmId == farmId }
    }
    val isLiked = currentFavorite?.isLiked ?: false
    val isSaved = currentFavorite?.isSaved ?: false

    // Si la granja no está cargada, mostrar loading
    if (farm == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("MineFarms", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }
    
    // En este punto sabemos que farm no es null
    val currentFarm = farm!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentFarm.name, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        userId?.let { uid ->
                            scope.launch {
                                val newLikedState = !isLiked
                                // Crear o actualizar el registro
                                val favorite = FavoriteEntity(
                                    userId = uid,
                                    farmId = farmId,
                                    isLiked = newLikedState,
                                    isSaved = isSaved
                                )
                                favoriteDao.insertFavorite(favorite)
                            }
                        }
                    }) {
                        Icon(
                            if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Me gusta",
                            tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { 
                        userId?.let { uid ->
                            scope.launch {
                                val newSavedState = !isSaved
                                // Crear o actualizar el registro
                                val favorite = FavoriteEntity(
                                    userId = uid,
                                    farmId = farmId,
                                    isLiked = isLiked,
                                    isSaved = newSavedState
                                )
                                favoriteDao.insertFavorite(favorite)
                            }
                        }
                    }) {
                        Icon(
                            if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Guardar",
                            tint = if (isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cargar imagen desde URI (granjas de usuario) o desde drawable (granjas predefinidas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (currentFarm.imageUri != null) {
                    // Imagen de galería (granja de usuario)
                    Image(
                        painter = rememberAsyncImagePainter(android.net.Uri.parse(currentFarm.imageUri)),
                        contentDescription = currentFarm.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val imageName = currentFarm.imageResourceName
                    val imageId = remember(imageName) {
                        ImageUtils.getDrawableId(context, imageName)
                    }
                    
                    if (imageId != 0) {
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = currentFarm.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("⛏️🌾", style = MaterialTheme.typography.displayLarge)
                    }
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(currentFarm.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard("⚙️", "Dificultad", currentFarm.difficulty, Modifier.weight(1f))
                InfoCard("📦", "Producción", currentFarm.productionRate, Modifier.weight(1f))
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🛠️ Materiales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    for (material in currentFarm.materials) {
                        Text("• $material", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✨ Produce", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(currentFarm.production, style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (currentFarm.process.isNotBlank()) {
                Card {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🔧 Proceso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(currentFarm.process, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (currentFarm.tags.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (tag in currentFarm.tags.take(3)) {
                        AssistChip(onClick = { }, label = { Text(tag) })
                    }
                }
            }

            if (currentFarm.tutorialUrl.isNotBlank()) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentFarm.tutorialUrl))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PlayArrow, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver Tutorial en YouTube")
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoCard(icon: String, label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(icon, style = MaterialTheme.typography.titleLarge)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
    }
}
