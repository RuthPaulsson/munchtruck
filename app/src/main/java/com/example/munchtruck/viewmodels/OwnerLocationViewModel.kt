package com.example.munchtruck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.TruckRepository
import com.example.munchtruck.data.repository.firebase.DeviceLocationProvider
import com.example.munchtruck.ui.owner.LocationError
import com.example.munchtruck.ui.owner.LocationUiState
import com.example.munchtruck.util.LocationConstants
import com.example.munchtruck.util.LocationValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import android.util.Log

class OwnerLocationViewModel(
    private val truckRepository: TruckRepository,
    private val locationProvider: DeviceLocationProvider,
    private val truckId: String
) : ViewModel() {

    private val validator = LocationValidator()
    private val TAG = "OwnerLocationViewModel"

    private val _uiState: MutableStateFlow<LocationUiState> = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadSavedLocation()
    }

    private fun loadSavedLocation() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val truck = truckRepository.getTruckById(truckId)
                truck.location?.let { location ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            selectedLat = location.latitude,
                            selectedLng = location.longitude,
                            address = location.adress,
                            isLoading = false
                        )
                    }
                } ?: run {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load saved location", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = LocationError.Unknown("Kunde inte ladda sparad plats")
                    )
                }
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                hasPermission = granted,
                error = if (!granted) LocationError.NoPermission else null
            )
        }
    }

    fun onManualAddressChanged(address: String) {
        val error = validator.validateAddress(address)
        _uiState.update { currentState ->
            currentState.copy(
                address = address,
                error = error
            )
        }
    }

    fun onMapPicked(lat: Double, lng: Double) {
        val error = validator.validateCoordinates(lat, lng)
        _uiState.update { currentState ->
            currentState.copy(
                selectedLat = lat,
                selectedLng = lng,
                error = error
            )
        }
    }

    fun useCurrentLocation() {
        if (!_uiState.value.hasPermission) {
            _uiState.update { currentState ->
                currentState.copy(error = LocationError.NoPermission)
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isLoading = true, error = null, saveSuccess = false)
        }

        viewModelScope.launch {
            try {
                val point = withTimeout(LocationConstants.GPS_TIMEOUT_MS) {
                    locationProvider.getCurrentLatLng()
                }
                delay(LocationConstants.LOADING_DELAY_MS)
                val error = validator.validateCoordinates(point.first, point.second)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        selectedLat = point.first,
                        selectedLng = point.second,
                        error = error
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = LocationError.GpsTimeout
                    )
                }
            } catch (e: SecurityException) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        hasPermission = false,
                        error = LocationError.NoPermission
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting current location", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = LocationError.Unknown(e.message ?: "Unknown error")
                    )
                }
            }
        }
    }

    fun saveLocation() {
        val state = _uiState.value
        val error = validator.validateCoordinates(state.selectedLat, state.selectedLng)

        if (error != null) {
            _uiState.update { currentState ->
                currentState.copy(error = error)
            }
            return
        }

        _uiState.update { currentState ->
            currentState.copy(isLoading = true, error = null, saveSuccess = false)
        }

        viewModelScope.launch {
            try {
                val location = TruckLocation(
                    latitude = state.selectedLat!!,
                    longitude = state.selectedLng!!,
                    adress = state.address,
                    updatedAtMilis = System.currentTimeMillis()
                )
                // truckRepository.updateTruckLocation(truckId, location)
                // Liten fördröjning för att visa loading-state
                delay(500)
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, saveSuccess = true)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save location", e)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = LocationError.SaveFailed
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(error = null)
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { currentState ->
            currentState.copy(saveSuccess = false)
        }
    }

    fun resetLocationForm() {
        _uiState.update { LocationUiState() }
    }

    fun hasSelectedLocation(): Boolean {
        return _uiState.value.selectedLat != null && _uiState.value.selectedLng != null
    }
}