package com.example.munchtruck.data.repository.firebase


import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseProfileRepository (
    private val firestore: FirebaseFirestore,
) : ProfileRepository {
    override suspend fun getTruckProfile(ownerUid: String): FoodTruck {
        TODO("Not yet implemented")
    }

    override suspend fun updateTruckProfile(
        ownerUid: String,
        name: String,
        description: String,
        location: String,
        isOpen: Boolean,
        imageUrl: String
    ): FoodTruck {
        TODO("Not yet implemented")
    }


}