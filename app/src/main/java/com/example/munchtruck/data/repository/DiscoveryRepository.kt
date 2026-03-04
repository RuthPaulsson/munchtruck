package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.FoodTruck
import kotlinx.coroutines.flow.Flow

// ====== Discovery Repository Interface ===============================
interface DiscoveryRepository {

    // ====== Data Streams ===============================
    fun observeOpenTrucks () : Flow<Result<List<FoodTruck>>>
}