package com.example.munchtruck.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.munchtruck.data.repository.TruckRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
class OwnerLocationViewModel(
    private val truckRepository: TruckRepository,
    private val locationProvider: DeviceLocationProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    // Lägg till denna funktion för validering
    private fun isValidLatLng(lat: Double?, lng: Double?): Boolean {
        if (lat == null || lng == null) return false
        if (lat < -90.0 || lat > 90.0) return false
        if (lng < -180.0 || lng > 180.0) return false
        if (lat == 0.0 && lng == 0.0) return false // Undvik default
        return true
    }

    // Resten av din kod är perfect!
    fun onPermissionResult(granted: Boolean) {
        _uiState.update {
            it.copy(
                hasPermission = granted,
                errorMessage = if (!granted) "Location permission denied" else null
            )
        }
    }

    fun onManualAddressChanged(address: String) {
        _uiState.update { it.copy(address = address, errorMessage = null) }
    }

    fun onMapPicked(lat: Double, lng: Double) {
        _uiState.update {
            it.copy(
                selectedLat = lat,
                selectedLng = lng,
                errorMessage = null
            )
        }
    }

    fun useCurrentLocation() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, saveSuccess = false) }

        viewModelScope.launch {
            try {
                val point = locationProvider.getCurrentLatLng()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedLat = point.first,
                        selectedLng = point.second
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to retrieve GPS location"
                    )
                }
            }
        }
    }

    fun saveLocation() {
        val state = _uiState.value
        if (!isValidLatLng(state.selectedLat, state.selectedLng)) {
            _uiState.update { it.copy(errorMessage = "Invalid location selected") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null, saveSuccess = false) }

        viewModelScope.launch {
            try {
                truckRepository.updateTruckLocation(
                    TruckLocation(
                        latitude = state.selectedLat!!,
                        longitude = state.selectedLng!!,
                        adress = state.address,
                        updatedAtMilis = System.currentTimeMillis()
                    )
                )
                _uiState.update { it.copy(isLoading = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to save location"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}