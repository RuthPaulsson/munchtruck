package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseDiscoveryRepository (
    private val firestore: FirebaseFirestore,
) : DiscoveryRepository {

    override fun observeOpenTrucks(): Flow<List<FoodTruck>> = callbackFlow {

        val listener = firestore.collection("foodTrucks")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val trucks = snapshots?.documents.orEmpty().map { doc ->
                    val name = doc.getString("name")?.trim().orEmpty()
                    val description = doc.getString("description").orEmpty()
                    val foodType = doc.getString("foodType").orEmpty()
                    val imageUrl = doc.getString("imageUrl").orEmpty()

                    val loc = doc.get("location") as? Map<*, *>
                    val lat = (loc?.get("latitude") as? Number)?.toDouble()
                    val long = (loc?.get("longitude") as? Number)?.toDouble()


                    if (
                        name.isBlank() ||
                        lat == null ||
                        long == null ||
                        lat !in -90.0..90.0 ||
                        long !in -180.0..180.0
                    ) {
                        null
                    } else {

                        FoodTruck(
                            id = doc.id,
                            name = name,
                            description = description,
                            foodType = foodType,
                            imageUrl = imageUrl,
                            location = TruckLocation(
                                latitude = lat,
                                longitude = long,
                                address = (loc["address"] as? String).orEmpty(),
                                updatedAt = (loc["updatedAt"] as? Number)?.toLong() ?: 0L
                            )
                        )
                    }

                }
                    .filterNotNull()

                trySend(trucks)
            }

            awaitClose { listener.remove()
            }


    }

}