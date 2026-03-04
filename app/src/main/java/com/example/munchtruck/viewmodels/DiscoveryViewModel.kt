package com.example.munchtruck.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.location.DeviceLocationProvider
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.example.munchtruck.data.repository.MenuRepository
import com.example.munchtruck.data.repository.ProfileRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ====== Discovery State Definitions ===============================

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
    val selectedCategory: String = ""

)

data class MapMarker(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double
)

// ====== Discovery ViewModel ===============================

class DiscoveryViewModel(
    private val discoveryRepository: DiscoveryRepository,
    private val locationProvider: DeviceLocationProvider,
    private val menuRepository: MenuRepository,
    profileRepository: ProfileRepository
) : ViewModel() {

    // ====== State & Initialization ===============================

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    private var menuJob: Job? = null

    private var allTrucks: List<FoodTruck> = emptyList()

    init {
        observeTrucks()
        startLocationUpdates()
    }



    // ====== Truck Observation & Menu Actions =======================

    fun selectedTruckAndLoadMenu(truckId: String) {
        menuJob?.cancel()
        _uiState.update { it.copy(isMenuLoading = true) }

        menuJob = viewModelScope.launch {
            menuRepository.observeTruckMenu(truckId).collect { result ->
                result.onSuccess { items ->
                    _uiState.update { it.copy(
                        selectedTruckMenu = items,
                        isMenuLoading = false
                    ) }
                }.onFailure {
                    _uiState.update { it.copy(
                        isMenuLoading = false,
                        error = DiscoveryError.LoadMenuFailed
                    ) }
                }
            }
        }
    }

    fun observeTrucks() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            discoveryRepository.observeOpenTrucks().collect { result ->
                result.onSuccess { trucks ->
                    // 1. Spara ner rådatan så vi kan filtrera på den senare utan att ladda om
                    allTrucks = trucks

                    // 2. Nollställ kategorin så att alla visas (enligt ditt önskemål vid refresh)
                    _uiState.update { currentState ->
                        currentState.copy(
                            selectedCategory = "", // Nollställer filtret
                            trucks = trucks,       // Visar alla
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
                }.onFailure {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = DiscoveryError.LoadTrucksFailed
                    ) }
                }
            }
        }
    }

//    fun observeTrucks() {
//        _uiState.update { it.copy(isLoading = true) }
//
//        viewModelScope.launch {
//            discoveryRepository.observeOpenTrucks().collect { result ->
//                result.onSuccess { trucks ->
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            trucks = trucks,
//                            isLoading = false,
//                            isListEmpty = trucks.isEmpty(),
//                            error = null,
//                            mapMarkers = trucks.map { truck ->
//                                MapMarker(
//                                    id = truck.id,
//                                    title = truck.name,
//                                    latitude = truck.location?.latitude ?: 0.0,
//                                    longitude = truck.location?.longitude ?: 0.0
//                                )
//                            }
//                        )
//                    }
//                }.onFailure {
//                    _uiState.update { it.copy(
//                        isLoading = false,
//                        error = DiscoveryError.LoadTrucksFailed
//                    ) }
//                }
//            }
//        }
//    }

    // ====== Location & Sorting Logic ===============================

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

    fun onCategorySelected(category: String) {
        // 1. Uppdatera statet med den nya valda kategorin (för att chippet ska lysa)
        _uiState.update { it.copy(selectedCategory = category) }

        // 2. Filtrera den sparade listan (allTrucks) baserat på valet
        val filteredList = if (category.isEmpty()) {
            allTrucks
        } else {
            allTrucks.filter { it.foodType?.trim().equals(category.trim(), ignoreCase = true) }
        }

        // 3. Sortera resultatet baserat på var användaren är just nu
        val sortedFiltered = sortTrucks(filteredList, _uiState.value.userLocation)

        // 4. Uppdatera UI-statet med den nya listan
        _uiState.update { currentState ->
            currentState.copy(
                trucks = sortedFiltered,
                isListEmpty = sortedFiltered.isEmpty()
            )
        }

        // 5. Uppdatera kartan så bara de rätta truckarna syns där med
        updateMapMarkers(sortedFiltered)
    }

    // ====== UI Helper Methods ===============================

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
}
