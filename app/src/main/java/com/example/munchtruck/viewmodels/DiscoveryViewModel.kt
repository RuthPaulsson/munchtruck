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
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.example.munchtruck.data.repository.MenuRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

data class DiscoveryUiState(
    val trucks: List<FoodTruck> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userLocation: Location? = null,
    val isListEmpty: Boolean = false,
    val selectedTruckMenu: List<MenuItem> = emptyList(),
    val isMenuLoading: Boolean = false
)

class DiscoveryViewModel(
    private val discoveryRepository: DiscoveryRepository,
    private val locationProvider: DeviceLocationProvider,
    private val menuRepository: MenuRepository
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
                    errorMessage = "Could not load menu: ${e.localizedMessage}"
                ) }
            }
        }
    }

    fun formatPrice(price: Long): String {
        return "$price kr"
    }

    init {
        observeTrucks()
        startLocationUpdates()
    }

    fun observeTrucks() {
        _uiState.update { it.copy(isLoading = true) }

        

        viewModelScope.launch {
            try {
                discoveryRepository.observeOpenTrucks()
                    .collect { trucks ->
                        _uiState.update {
                            it.copy(
                                trucks = trucks,
                                isLoading = false,
                                isListEmpty = trucks.isEmpty(),
                                errorMessage = null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Failed to load trucks"
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
                            trucks = sortTrucks(currentState.trucks, newLocation)
                        )
                    }
                    delay(10000)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Could not fetch your location") }
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
