package com.example.munchtruck.ui.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.munchtruck.viewmodels.DiscoveryViewModel

@Composable
fun DiscoveryScreen(
    viewModel: DiscoveryViewModel,
    onTruckClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DiscoveryContent(
        uiState = uiState,
        onRefresh = { viewModel.observeTrucks() },
        onTruckClick = onTruckClick
    )
}