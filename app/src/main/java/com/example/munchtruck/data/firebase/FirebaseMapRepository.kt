package com.example.munchtruck.data.map

import com.example.munchtruck.data.model.MapTruck
import com.example.munchtruck.data.repository.MapRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseMapRepository (
    private val firestore: FirebaseFirestore,
): MapRepository {


    override fun observeActiveTrucksForMap(): Flow<Result<List<MapTruck>>> = callbackFlow {

        val listener = firestore.collection("mapTrucks")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot == null || snapshot.isEmpty) {
                    trySend(Result.success(emptyList()))
                    return@addSnapshotListener
                }

                val trucks = snapshot.documents.mapNotNull { document ->
                    try {
                        val name = document.getString("name") ?: return@mapNotNull null
                        val foodType = document.getString("foodType") ?: ""
                        val imageUrl = document.getString("imageUrl") ?: ""
                        val isActive = document.getBoolean("isActive") ?: false

                        val latitude = document.getDouble("latitude") ?: return@mapNotNull null
                        val longitude = document.getDouble("longitude") ?: return@mapNotNull null

                        if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
                            return@mapNotNull null
                        }

                        MapTruck(
                            id = document.id,
                            name = name,
                            latitude = latitude,
                            longitude = longitude,
                            foodType = foodType,
                            imageUrl = imageUrl,
                            isActive = isActive
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                trySend(Result.success(trucks))
            }

        awaitClose { listener.remove() }
    }
}