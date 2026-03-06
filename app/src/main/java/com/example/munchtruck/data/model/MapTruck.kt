package com.example.munchtruck.data.model

/**
 * Optimized model for displaying trucks on the map.
 * Contains only the essential data needed for map markers and basic previews.
 */
data class MapTruck(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val foodType: String,
    val imageUrl: String,
    val openingHours: OpeningHours? = null
)