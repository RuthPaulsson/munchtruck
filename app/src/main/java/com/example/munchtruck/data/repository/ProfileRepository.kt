package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck

interface ProfileRepository {
    suspend fun getTruckProfile (): FoodTruck

    suspend fun updateMyTruckProfile (
        name: String,
        description: String,
        location: String,
        imageUrl: String,
        isOpen: Boolean,
        foodType: String
    ): FoodTruck

}