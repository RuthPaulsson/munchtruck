package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class FirebaseDiscoveryRepository (
    private val firestore: FirebaseFirestore,
) : DiscoveryRepository {

    override fun observeOpenTruck(): Flow<List<FoodTruck>> {
        TODO("Not yet implemented")
    }

}