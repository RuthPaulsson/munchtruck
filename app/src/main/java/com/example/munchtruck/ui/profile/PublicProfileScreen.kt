package com.example.munchtruck.ui.profile


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.CenteredMessage
import com.example.munchtruck.ui.components.CenteredMessageWithRetry
import com.example.munchtruck.ui.components.toDisplayString
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.DiscoveryError
import com.example.munchtruck.viewmodels.DiscoveryViewModel

@Composable
fun PublicProfileScreen(
    truckId: String,
    discoveryViewModel: DiscoveryViewModel,

) {
    val uiState by discoveryViewModel.uiState.collectAsStateWithLifecycle()
    val errorMessage = uiState.error?.toMessage()

    LaunchedEffect(truckId) {
        discoveryViewModel.selectedTruckAndLoadMenu(truckId)
    }

    val truck = uiState.trucks.find { it.id == truckId }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            // Visa laddningsskärm om menyn hämtas för första gången
            uiState.isMenuLoading && uiState.selectedTruckMenu.isEmpty() -> {
                CenteredLoading()
            }

            errorMessage != null -> {
                CenteredMessageWithRetry(
                    message = errorMessage,
                    onRetry = {
                        discoveryViewModel.selectedTruckAndLoadMenu(truckId)
                    }
                )
            }

            truck == null -> {
                CenteredMessage(
                    stringResource(R.string.public_profile_truck_not_found)
                )
            }

            uiState.selectedTruckMenu.isEmpty() -> {
                CenteredMessage(
                    stringResource(R.string.public_profile_menu_empty)
                )
            }

            else -> {
                ProfileContent(
                    isOwner = false,
                    truckName = truck.name,
                    description = truck.description,
                    rating = null,
                    location = truck.location?.address,
                    openingHours = truck.openingHours?.toDisplayString(),
                    imageUrl = truck.imageUrl,
                    menuItems = uiState.selectedTruckMenu,
                    isLoading = uiState.isMenuLoading,
                    errorMessage = null,
                    onLogoutClick = {},
                    onEditClick = {}
                )
            }
        }
    }
}