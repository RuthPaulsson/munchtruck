package com.example.munchtruck.data.repository.firebase

interface DeviceLocationProvider {
    suspend fun getCurrentLatLng(): Pair<Double, Double>
}