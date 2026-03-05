package com.example.munchtruck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.LocationConstants
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.ProfileRepository  // Rätt!
import com.example.munchtruck.data.location.DeviceLocationProvider
import com.example.munchtruck.util.LocationValidator
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

    // ====== Location State Definitions ===============================

sealed class LocationError {
    object NoPermission : LocationError()
    object NoLocation : LocationError()
    object InvalidCoordinates : LocationError()
    object GpsTimeout : LocationError()
    object AddressTooShort : LocationError()
    object SaveFailed : LocationError()
    data object LoadFailed : LocationError()
    data class Unknown(val message: String?) : LocationError()
}

data class LocationUiState(
    val isLoading: Boolean = false,
    val selectedLat: Double? = null,
    val selectedLng: Double? = null,
    val address: String = "",
    val hasPermission: Boolean = false,
    val error: LocationError? = null,
    val saveSuccess: Boolean = false
)

    // ====== Location ViewModel ===============================

class LocationViewModel(
    private val profileRepository: ProfileRepository,
    private val locationProvider: DeviceLocationProvider
) : ViewModel() {

    // ====== State & Initialization ===============================

    private val validator = LocationValidator()
    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        loadSavedLocation()
    }

    // ====== Load Data ===============================

    private fun loadSavedLocation() {
        viewModelScope.launch {
            try {
                val truck = profileRepository.getTruckProfile()
                truck.location?.let { location ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            selectedLat = location.latitude,
                            selectedLng = location.longitude,
                            address = location.address,
                            isLoading = false
                        )
                    }
                } ?: run {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = LocationError.LoadFailed
                    )
                }
            }
        }
    }

    // ====== Permission Actions ===============================

    fun onPermissionResult(granted: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                hasPermission = granted,
                error = if (!granted) LocationError.NoPermission else null
            )
        }
    }

    // ====== Manual Input & Selection ===========================

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
        if (error == null) {
            viewModelScope.launch {
                val addressString = locationProvider.getAddressFromCords(lat, lng)
                _uiState.update { it.copy(address = addressString) }
            }
        }
    }

    // ====== GPS Actions ===============================

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


                val addressString = locationProvider.getAddressFromCords(point.first, point.second)

                val error = validator.validateCoordinates(point.first, point.second)

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        selectedLat = point.first,
                        selectedLng = point.second,
                        address = addressString,
                        error = error
                    )
                }
            } catch (e: TimeoutCancellationException) {
                _uiState.update { it.copy(isLoading = false, error = LocationError.GpsTimeout) }
            } catch (e: SecurityException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasPermission = false,
                        error = LocationError.NoPermission
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = LocationError.Unknown(e.message)
                    )
                }
            }
        }
    }

    // ====== Save Actions ===============================

    fun saveLocation() {

        val state = _uiState.value
        val validationError: LocationError? =
            validator.validateCoordinates(state.selectedLat, state.selectedLng)
                ?: validator.validateAddress(state.address)


        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
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
                    address = state.address,
                    updatedAt = System.currentTimeMillis()
                )
                profileRepository.updateMyTruckLocation(location)

                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, saveSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = LocationError.SaveFailed
                    )
                }
            }
        }
    }

    // ====== UI State Helpers ===============================

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
