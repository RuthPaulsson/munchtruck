package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.model.MapTruck
import com.example.munchtruck.data.repository.MapRepository
import com.example.munchtruck.data.toFoodTruck
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseMapRepository(
    private val firestore: FirebaseFirestore,
) : MapRepository {


    override fun observeActiveTrucksForMap(): Flow<Result<List<MapTruck>>> = callbackFlow {

        val listener = firestore.collection("mapTrucks")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                val trucks = snapshot?.documents?.mapNotNull { doc ->
                    val truck = doc.toFoodTruck() ?: return@mapNotNull null
                    val loc = truck.location ?: return@mapNotNull null

                    MapTruck(
                        id = truck.id,
                        name = truck.name,
                        latitude = loc.latitude,
                        longitude = loc.longitude,
                        foodType = truck.foodType,
                        imageUrl = truck.imageUrl,
                        openingHours = truck.openingHours
                    )
                } ?: emptyList()

                trySend(Result.success(trucks))
            }

        awaitClose { listener.remove() }
    }
}