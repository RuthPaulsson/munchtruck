package com.example.munchtruck.ui.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.munchtruck.viewmodels.DiscoveryViewModel

@Composable
fun DiscoveryScreen(
    viewModel: DiscoveryViewModel,
    onTruckClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    DiscoveryContent(
        uiState = uiState,
        searchQuery = searchQuery,
        onSearchChange = { searchQuery = it },
        onRefresh = { viewModel.observeTrucks() },
        onTruckClick = onTruckClick
    )
}