package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.TruckLocation

interface TruckRepository {
    suspend fun updateMyTruckLocation(location: TruckLocation)
}