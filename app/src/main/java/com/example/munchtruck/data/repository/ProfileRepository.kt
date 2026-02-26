package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.model.TruckLocation

interface ProfileRepository {
    suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
//        isOpen: Boolean
    )

    suspend fun getTruckProfile (): FoodTruck

    suspend fun updateMyTruckLocation(location: TruckLocation)

    suspend fun updateMyTruckOpeningHours(hours: OpeningHours)
    suspend fun updateActiveStatus(isActive: Boolean)
}