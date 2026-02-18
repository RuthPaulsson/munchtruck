package com.example.munchtruck.data.repository.firebase

import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.TruckRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseTruckRepository (
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : TruckRepository {

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

    private fun myTruckDoc() = firestore.collection("foodTrucks")
        .document(truckId())



    override suspend fun updateMyTruckLocation(location: TruckLocation) {
    }
}