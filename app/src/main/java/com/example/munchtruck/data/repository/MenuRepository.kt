package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    fun getMenu(truckId: String): Flow<List<MenuItem>>

    suspend fun addMenuItem(
        truckId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    )

    suspend fun updateMenuItem(
        truckId: String,
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    )

    suspend fun deleteMenuItem(truckId: String, itemId: String)
}
