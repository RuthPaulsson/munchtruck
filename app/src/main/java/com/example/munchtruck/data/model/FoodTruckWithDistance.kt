package com.example.munchtruck.data.model

/**
 * A wrapper class that pairs a FoodTruck with its calculated distance from the user.
 * Primarily used in lists where sorting by proximity is required.
 */
data class FoodTruckWithDistance(
    val foodTruck: FoodTruck,
    val distanceInMeters: Float
)