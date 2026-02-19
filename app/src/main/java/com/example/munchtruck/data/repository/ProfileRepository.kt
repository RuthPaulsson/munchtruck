package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck

interface ProfileRepository {

    suspend fun saveTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
//        isOpen: Boolean
    ): FoodTruck

    suspend fun getTruckProfile (): FoodTruck


}