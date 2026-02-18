package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck

interface ProfileRepository {

    suspend fun saveProfile(name: String, description: String, foodType: String, imageUri: android.net.Uri?)

    suspend fun getTruckProfile (): FoodTruck

    suspend fun updateTruckProfile (
        name: String,
        description: String,
        location: String,
        imageUrl: String,
        isOpen: Boolean,
        foodType: String
    ): FoodTruck

}