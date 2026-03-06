package com.example.munchtruck.ui.discovery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.DiscoveryViewModel

// ====== Discovery Screen (Logic Layer) ===============================

@Composable
fun DiscoveryScreen(
    navController: NavHostController,
    viewModel: DiscoveryViewModel,
    onTruckClick: (String) -> Unit
) {
    // ====== State & Initialization ===============================

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error?.toMessage()

    // ====== UI Rendering ===============================

    DiscoveryContent(
        uiState = uiState,
        errorMessage = errorMessage,
        selectedCategory = uiState.selectedCategory,
        onCategoryChange = { category ->
            viewModel.onCategorySelected(category)
        },
        onRefresh = {
            viewModel.observeTrucks()
        },
        onTruckClick = onTruckClick,
        onMapClick = {
            navController.navigate("map_view")
        },
        onHomeClick = {
            // Already on home
        }
    )
}