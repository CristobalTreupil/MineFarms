package com.example.minefarms.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.minefarms.data.database.AppDatabase
import com.example.minefarms.data.entity.UserFarmEntity
import com.example.minefarms.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFarmScreen(
    onBack: () -> Unit,
    onFarmCreated: () -> Unit,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val userId = authState.userId
    
    val scope = rememberCoroutineScope()
    val database = remember { AppDatabase.getDatabase(context) }
    val userFarmDao = database.userFarmDao()
    
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var materials by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Fácil") }
    var production by remember { mutableStateOf("") }
    var tutorialUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Launcher para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Granja", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de imagen
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "📷 Foto de la Granja",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.AddPhotoAlternate,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Toca para seleccionar imagen",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    
                    if (selectedImageUri != null) {
                        TextButton(
                            onClick = { selectedImageUri = null },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Quitar imagen")
                        }
                    }
                }
            }
            
            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la Granja *") },
                placeholder = { Text("Ej: Granja de Trigo Automática") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción *") },
                placeholder = { Text("Describe cómo funciona tu granja...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
            
            // Materiales
            OutlinedTextField(
                value = materials,
                onValueChange = { materials = it },
                label = { Text("Materiales (separados por coma)") },
                placeholder = { Text("Ej: Pistones, Redstone, Observadores") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            
            // Dificultad
            var expandedDifficulty by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedDifficulty,
                onExpandedChange = { expandedDifficulty = it }
            ) {
                OutlinedTextField(
                    value = difficulty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Dificultad") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDifficulty) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedDifficulty,
                    onDismissRequest = { expandedDifficulty = false }
                ) {
                    listOf("Fácil", "Media", "Difícil", "Muy Difícil").forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) },
                            onClick = {
                                difficulty = level
                                expandedDifficulty = false
                            }
                        )
                    }
                }
            }
            
            // Producción
            OutlinedTextField(
                value = production,
                onValueChange = { production = it },
                label = { Text("Qué Produce") },
                placeholder = { Text("Ej: Trigo, experiencia") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // URL del tutorial (opcional)
            OutlinedTextField(
                value = tutorialUrl,
                onValueChange = { tutorialUrl = it },
                label = { Text("Link de YouTube (opcional)") },
                placeholder = { Text("https://youtube.com/watch?v=...") },
                leadingIcon = {
                    Icon(Icons.Default.VideoLibrary, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Botón de crear
            Button(
                onClick = {
                    if (name.isNotBlank() && description.isNotBlank() && userId != null) {
                        isLoading = true
                        scope.launch {
                            try {
                                val userFarm = UserFarmEntity(
                                    userId = userId,
                                    name = name.trim(),
                                    description = description.trim(),
                                    materials = materials.trim(),
                                    difficulty = difficulty,
                                    production = production.trim(),
                                    tutorialUrl = tutorialUrl.trim().ifBlank { null },
                                    imageUri = selectedImageUri?.toString(),
                                    createdAt = System.currentTimeMillis()
                                )
                                userFarmDao.insertFarm(userFarm)
                                isLoading = false
                                showSuccessDialog = true
                            } catch (e: Exception) {
                                isLoading = false
                                e.printStackTrace()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && name.isNotBlank() && description.isNotBlank(),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Upload, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear Granja", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            Text(
                "* Campos obligatorios",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    
    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¡Granja Creada!") },
            text = { Text("Tu granja se ha publicado exitosamente y ya está visible en tu perfil.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onFarmCreated()
                    }
                ) {
                    Text("Ver en Perfil")
                }
            }
        )
    }
}
