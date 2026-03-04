package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

// ====== Menu Repository Interface ===============================
interface MenuRepository {

    // ====== Data Streams ===============================
    fun observeMyMenu(): Flow<Result<List<MenuItem>>>

    fun observeTruckMenu(truckId: String): Flow<Result<List<MenuItem>>>

    // ====== Menu Operations ===============================
    suspend fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) : String

    suspend fun updateMenuItem(
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    )

    suspend fun deleteMenuItem(itemId: String)
}
