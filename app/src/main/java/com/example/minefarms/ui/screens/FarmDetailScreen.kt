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
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.data.entity.FavoriteEntity
import com.example.minefarms.repository.FarmRepository
import com.example.minefarms.ui.viewmodel.AuthViewModel
import com.example.minefarms.utils.ImageUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmDetailScreen(
    farmId: Int,
    onBack: () -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val farm = FarmRepository.getFarmById(farmId)
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val userId = authState.userId
    
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val favoriteDao = database.favoriteDao()
    
    var isLiked by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }
    
    // Cargar estado inicial desde la base de datos
    LaunchedEffect(farmId, userId) {
        userId?.let { uid ->
            favoriteDao.getFavoritesByUser(uid).collect { favorites ->
                val favorite = favorites.find { it.farmId == farmId }
                isLiked = favorite?.isLiked == true
                isSaved = favorite?.isSaved == true
            }
        }
    }

    if (farm == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Granja no encontrada")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(farm.name, fontWeight = FontWeight.Bold) },
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
                                isLiked = newLikedState
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
                                isSaved = newSavedState
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
            val imageId = ImageUtils.getDrawableId(context, farm.imageResourceName)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (imageId != 0) {
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = farm.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("⛏️🌾", style = MaterialTheme.typography.displayLarge)
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(farm.description, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard("⚙️", "Dificultad", farm.difficulty, Modifier.weight(1f))
                InfoCard("📦", "Producción", farm.productionRate, Modifier.weight(1f))
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("��️ Materiales", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    farm.materials.forEach { material ->
                        Text("• $material", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✨ Produce", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(farm.production, style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (farm.process.isNotBlank()) {
                Card {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🔧 Proceso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(farm.process, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (farm.tags.isNotEmpty()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    farm.tags.take(3).forEach { tag ->
                        AssistChip(onClick = { }, label = { Text(tag) })
                    }
                }
            }

            if (farm.tutorialUrl.isNotBlank()) {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(farm.tutorialUrl))
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
