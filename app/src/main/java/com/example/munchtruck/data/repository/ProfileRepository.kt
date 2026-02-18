package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck

interface ProfileRepository {

    suspend fun saveProfile(name: String, description: String, foodType: String, imageUri: android.net.Uri?)

    suspend fun getTruckProfile (ownerUid: String): FoodTruck

    suspend fun updateTruckProfile (
        id: String,
        name: String,
        description: String,
        location: String,
        imageUrl: String,
        isOpen: Boolean,
        ownerId: String
    ): FoodTruck

}