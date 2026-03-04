package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.model.TruckLocation

// ====== Profile Repository Interface ===============================
interface ProfileRepository {

    // ====== Profile Management ===============================
    suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
        openingHours: OpeningHours?
//        isOpen: Boolean
    )

    suspend fun getTruckProfile (): FoodTruck

    // ====== Operational Updates ===============================
    suspend fun updateMyTruckLocation(location: TruckLocation)

    suspend fun updateMyTruckOpeningHours(hours: OpeningHours)

    // ====== Data Cleanup ===============================
    suspend fun deleteAllTruckData()

}