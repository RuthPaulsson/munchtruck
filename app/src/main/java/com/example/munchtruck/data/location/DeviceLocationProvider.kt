package com.example.munchtruck.data.location

interface DeviceLocationProvider {
    suspend fun getCurrentLatLng(): Pair<Double, Double>

    suspend fun getAddressFromCords(lat: Double, lng: Double): String}