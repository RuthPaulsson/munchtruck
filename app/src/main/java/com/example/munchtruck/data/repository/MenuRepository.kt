package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {


    fun observeMyMenu(): Flow<List<MenuItem>> // FoodTruck owner

    fun observeTruckMenu(truckId: String): Flow<List<MenuItem>> // FoodLover guest

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
