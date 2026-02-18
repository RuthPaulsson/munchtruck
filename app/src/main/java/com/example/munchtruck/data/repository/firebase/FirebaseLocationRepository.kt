package com.example.munchtruck.data.repository.firebase

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.LocationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow

class FirebaseLocationRepository (
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : LocationRepository {
    override fun observeMyTruck(): Flow<FoodTruck> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMyTruckLocation(location: TruckLocation) {
        TODO("Not yet implemented")
    }
}