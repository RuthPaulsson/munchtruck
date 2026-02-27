package com.example.munchtruck.data.model

data class FoodTruck (
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val foodType: String = "",
    val location: TruckLocation? = null,
    val imageUrl: String = "",
    val openingHours: OpeningHours? = null,
    val isOpen: Boolean = false,
    val isActive: Boolean = true
)