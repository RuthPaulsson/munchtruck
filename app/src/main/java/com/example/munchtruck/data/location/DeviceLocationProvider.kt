package com.example.munchtruck.data.location

// ====== Device Location Provider Interface ===============================
interface DeviceLocationProvider {

    // ====== Location Methods ===============================
    suspend fun getCurrentLatLng(): Pair<Double, Double>

    // ====== Geocoding Methods ===============================
    suspend fun getAddressFromCords(lat: Double, lng: Double): String}