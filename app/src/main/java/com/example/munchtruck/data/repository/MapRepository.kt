package com.example.munchtruck.data.repository
import com.example.munchtruck.data.model.MapTruck
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun observeActiveTrucksForMap(): Flow<Result<List<MapTruck>>>
}