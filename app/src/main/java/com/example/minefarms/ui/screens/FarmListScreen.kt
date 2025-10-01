package com.example.minefarms.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.minefarms.model.Farm
import com.example.minefarms.viewmodel.FarmViewModel
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun FarmListScreen(
    viewModel: FarmViewModel,
    onFarmClick: (Int) -> Unit
) {
    // ✅ Usamos collectAsState para observar el StateFlow
    val farms by viewModel.farms.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("MineFarms Wiki") }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            items(farms) { farm -> // ✅ Aquí 'farm' es del tipo 'Farm'
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onFarmClick(farm.id) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(farm.name, style = MaterialTheme.typography.titleMedium)
                        Text(farm.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}