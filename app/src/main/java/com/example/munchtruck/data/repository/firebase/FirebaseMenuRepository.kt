package com.example.munchtruck.data.repository.firebase

import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class FirebaseMenuRepository (
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : MenuRepository {
    override fun getMenu(truckId: String): Flow<List<MenuItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMenuItem(
        truckId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateMenuItem(
        truckId: String,
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMenuItem(truckId: String, itemId: String) {
        TODO("Not yet implemented")
    }
}