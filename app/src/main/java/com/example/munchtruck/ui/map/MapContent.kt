package com.example.munchtruck.ui.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.ui.components.DiscoveryBottom
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.viewmodels.DiscoveryUiState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapContent(
    uiState: DiscoveryUiState,
    onTruckClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(uiState.userLocation) {
        uiState.userLocation?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 13f
            )
        }
    }
    Scaffold(
        bottomBar = {
            DiscoveryBottom(
                currentRoute = "map",
                onMapClick = { /* We are here */ },
                onHomeClick = onNavigateToHome
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = uiState.userLocation != null
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false
                )
            ) {
                uiState.mapMarkers.forEach { markerData ->
                  Marker(
                      state = MarkerState(
                          position = LatLng(
                              markerData.latitude,
                              markerData.longitude)
                      ),
                      title = markerData.title,
                      onInfoWindowClick = { onTruckClick(markerData.id) }
                  )
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color= PrimaryOrange
                )
            }
        }

    }

}
// ====== Preview ===============================
@Preview(showBackground = true)
@Composable
fun MapContentPreview() {
    val mockUiState = DiscoveryUiState(
        mapMarkers = emptyList(),
        isLoading = false,
        userLocation = null
    )

    AppPreviewWrapper {
        MapContent(
            uiState = mockUiState,
            onTruckClick = {},
            onNavigateToHome = {},
        )
    }
}