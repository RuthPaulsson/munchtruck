package com.example.munchtruck.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.FoodTruck
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.location.Location
import com.example.munchtruck.data.location.DeviceLocationProvider
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.model.isCurrentlyOpen
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.example.munchtruck.data.repository.MenuRepository
import com.example.munchtruck.data.repository.ProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

sealed class DiscoveryError {
    data object LocationPermissionDenied : DiscoveryError()
    data object LocationFetchFailed : DiscoveryError()
    data object LoadTrucksFailed : DiscoveryError()
    data object LoadMenuFailed : DiscoveryError()
}
data class DiscoveryUiState(
    val trucks: List<FoodTruck> = emptyList(),
    val isLoading: Boolean = false,
    val error: DiscoveryError? = null,
    val userLocation: Location? = null,
    val isListEmpty: Boolean = false,
    val selectedTruckMenu: List<MenuItem> = emptyList(),
    val isMenuLoading: Boolean = false,
    val mapMarkers: List<MapMarker> = emptyList(),
    val isMapLoading: Boolean = false,

)

data class MapMarker(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double
)

class DiscoveryViewModel(
    private val discoveryRepository: DiscoveryRepository,
    private val locationProvider: DeviceLocationProvider,
    private val menuRepository: MenuRepository,
    profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()
    private var menuJob: Job? = null

    fun selectedTruckAndLoadMenu(truckId: String) {
        menuJob?.cancel()

        _uiState.update { it.copy(isMenuLoading = true) }

        menuJob = viewModelScope.launch {
            try {
                menuRepository.observeTruckMenu(truckId).collect { items ->
                    _uiState.update { it.copy(
                        selectedTruckMenu = items,
                        isMenuLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isMenuLoading = false,
                    error = DiscoveryError.LoadMenuFailed
                ) }
            }
        }
    }

    init {
        observeTrucks()
        startLocationUpdates()
    }

    private fun updateMapMarkers(trucks: List<FoodTruck>) {
        val markers = trucks.map { truck ->
            MapMarker(
                id = truck.id,
                title = truck.name,
                latitude = truck.location?.latitude ?: 0.0,
                longitude = truck.location?.longitude ?: 0.0
            )
        }
        _uiState.update { it.copy(mapMarkers = markers) }
    }
    fun observeTrucks() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                discoveryRepository.observeOpenTrucks()
                    .collect { trucks ->
                        val processedTrucks = trucks.map { truck ->
                            val isActuallyOpen = if (!truck.isActive) {
                                false
                            } else {
                                truck.openingHours?.isCurrentlyOpen() ?: false
                            }
                            truck.copy(isOpen = isActuallyOpen)
                        }
                        _uiState.update { currentState ->
                            currentState.copy(
                                trucks = trucks,
                                isLoading = false,
                                isListEmpty = trucks.isEmpty(),
                                error = null,
                                mapMarkers = trucks.map { truck ->
                                MapMarker(
                                    id = truck.id,
                                    title = truck.name,
                                    latitude = truck.location?.latitude ?: 0.0,
                                    longitude = truck.location?.longitude ?: 0.0
                                )
                                }
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = DiscoveryError.LoadTrucksFailed
                    )
                }
            }
        }
    }

    fun startLocationUpdates() {
        viewModelScope.launch {
            try {
                while (true) {
                    val point = locationProvider.getCurrentLatLng()
                    val newLocation = Location("").apply {
                        latitude = point.first
                        longitude = point.second
                    }

                    _uiState.update { currentState ->
                        currentState.copy(
                            userLocation = newLocation,
                            trucks = sortTrucks(currentState.trucks, newLocation),
                            error = null
                        )
                    }
                    delay(10000)
                }
            } catch (e: SecurityException) {
                _uiState.update { it.copy(
                    error = DiscoveryError.LocationPermissionDenied
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(error = DiscoveryError.LocationFetchFailed) }
            }
        }
    }

    private fun sortTrucks(
        trucks: List<FoodTruck>,
        location: Location?
    ): List<FoodTruck> {

        if (location == null) return trucks

        return trucks

            .filter { it.location != null }

            .sortedBy { truck ->

                val loc = truck.location ?: return@sortedBy Double.MAX_VALUE

                val truckLoc = Location("").apply {
                    latitude = loc.latitude
                    longitude = loc.longitude
                }

                location.distanceTo(truckLoc).toDouble()
            }
    }
}
