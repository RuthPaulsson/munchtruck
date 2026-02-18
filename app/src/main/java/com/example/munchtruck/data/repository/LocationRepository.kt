package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun observeMyTruck(): Flow<FoodTruck>
    suspend fun updateMyTruckLocation(location: TruckLocation)
}