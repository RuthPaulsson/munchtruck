package com.example.munchtruck.data.repository
import com.example.munchtruck.data.model.MapTruck
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the contract for map-related data operations.
 * Primarily handles real-time tracking of trucks for map visualization.
 */
interface MapRepository {
    fun observeActiveTrucksForMap(): Flow<Result<List<MapTruck>>>
}