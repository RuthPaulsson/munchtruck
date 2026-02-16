package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck

interface ProfileRepository {
    suspend fun getTruckProfile (ownerUid: String): FoodTruck

    suspend fun updateTruckProfile (
        ownerUid: String,
        name: String,
        description: String,
        location: String,
        isOpen: Boolean,
        imageUrl: String
    ): FoodTruck

}