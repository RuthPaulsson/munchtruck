package com.example.munchtruck.data.model

data class MapTruck(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val foodType: String,
    val imageUrl: String,
    val isActive: Boolean,
    val openingHours: OpeningHours? = null
)