package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck
import kotlinx.coroutines.flow.Flow

interface DiscoveryRepository {
    fun observeOpenTrucks () : Flow<List<FoodTruck>>
}