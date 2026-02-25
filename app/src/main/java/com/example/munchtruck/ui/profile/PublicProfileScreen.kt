package com.example.munchtruck.ui.profile


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.CenteredMessage
import com.example.munchtruck.ui.components.CenteredMessageWithRetry
import com.example.munchtruck.viewmodels.DiscoveryViewModel

@Composable
fun PublicProfileScreen(
    truckId: String,
    discoveryViewModel: DiscoveryViewModel,

) {
    val uiState by discoveryViewModel.uiState.collectAsStateWithLifecycle()
    val error = uiState.error

    LaunchedEffect(truckId) {
        discoveryViewModel.selectedTruckAndLoadMenu(truckId)
    }

    val truck = uiState.trucks.find { it.id == truckId }

    when {
        uiState.isMenuLoading -> {
            CenteredLoading()
        }

        error != null -> {
            CenteredMessageWithRetry(
                message = error,
                onRetry = {
                    discoveryViewModel.selectedTruckAndLoadMenu(truckId)
                }
            )
        }
        truck == null -> {
            CenteredMessage("Truck not found")
        }

        uiState.selectedTruckMenu.isEmpty() -> {
            CenteredMessage("This truck has no menu items yet")
        }


        else -> {
            ProfileContent(
                isOwner = false,
                truckName = truck.name,
                description = truck.description,
                rating = null,
                location = truck.location?.address,
                openingHours = null,
                imageUrl = truck.imageUrl,
                menuItems = uiState.selectedTruckMenu,
                onLogoutClick = {},
                onEditClick = {}
            )
        }
    }
}