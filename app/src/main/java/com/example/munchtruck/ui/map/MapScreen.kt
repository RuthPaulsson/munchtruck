package com.example.munchtruck.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.munchtruck.viewmodels.DiscoveryViewModel

// ====== Map Screen (Logic Layer) ===============================

@Composable
fun MapScreen(
    viewModel: DiscoveryViewModel,
    onTruckClick: (String) -> Unit,
    onNavigateToHome: () -> Unit
) {
    // ====== State & Initialization ===============================

    val uiState by viewModel.uiState.collectAsState()

    // ====== UI Rendering ===============================

    MapContent(
        uiState = uiState,
        onTruckClick = onTruckClick,
        onNavigateToHome = onNavigateToHome,
    )
}