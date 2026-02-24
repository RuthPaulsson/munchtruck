package com.example.munchtruck.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.ProfileRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.location.Location
import com.example.munchtruck.data.location.DeviceLocationProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.delay

data class DiscoveryUiState(
    val trucks: List<FoodTruck> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userLocation: Location? = null,
    val isListEmpty: Boolean = false
)

class DiscoveryViewModel(
    private val profileRepository: ProfileRepository,
    private val locationProvider: DeviceLocationProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    init {
        observeTrucks()
        startLocationUpdates()
    }

    fun observeTrucks() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val allTrucks = profileRepository.getAllTrucks()
                _uiState.update {
                    it.copy(
                        trucks = allTrucks,
                        isLoading = false,
                        isListEmpty = allTrucks.isEmpty()
                    )
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

    fun sortTrucks(trucks: List<FoodTruck>, location: Location?): List<FoodTruck> {
        if (location == null) return trucks

        return trucks.sortedBy { truck ->
            val truckLocation = Location("").apply {
                latitude = truck.location?.latitude ?: 0.0
                longitude = truck.location?.longitude ?: 0.0
            }
            location.distanceTo(truckLocation)
        }
    }
}
