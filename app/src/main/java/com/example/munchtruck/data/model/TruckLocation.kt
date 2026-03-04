package com.example.munchtruck.data.model

/**
 * Represents the geographical location and address of a food truck.
 * Used for map placement and distance calculations.
 */
data class TruckLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val updatedAt: Long = 0L
)
