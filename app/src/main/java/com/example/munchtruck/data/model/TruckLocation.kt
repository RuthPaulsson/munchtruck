package com.example.munchtruck.data.model

data class TruckLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = "",
    val updatedAt: Long = 0L
)
