package com.example.munchtruck.data.model

/**
 * Full data model for a Food Truck.
 * Contains comprehensive information including location, menu items (implied), and status.
 */
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