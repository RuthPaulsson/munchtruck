package com.example.munchtruck.data.repository

import com.example.munchtruck.data.model.MenuItem
import kotlinx.coroutines.flow.Flow

interface MenuRepository {

    fun observeMenu(): Flow<List<MenuItem>>

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
