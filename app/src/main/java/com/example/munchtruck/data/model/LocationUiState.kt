package com.example.munchtruck.ui.owner

data class LocationUiState(
    val isLoading: Boolean = false,
    val selectedLat: Double? = null,
    val selectedLng: Double? = null,
    val address: String = "",
    val hasPermission: Boolean = false,
    val error: LocationError? = null,
    val saveSuccess: Boolean = false
)