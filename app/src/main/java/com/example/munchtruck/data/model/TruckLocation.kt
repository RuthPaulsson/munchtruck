package com.example.munchtruck.data.model

data class TruckLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val adress: String = "",
    val updatedAtMilis: Long = 0L
)
