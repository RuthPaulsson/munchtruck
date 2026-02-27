package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.example.munchtruck.data.toFoodTruck
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseDiscoveryRepository(
    private val firestore: FirebaseFirestore,
) : DiscoveryRepository {

    override fun observeOpenTrucks(): Flow<Result<List<FoodTruck>>> = callbackFlow {

        val listener = firestore.collection(FirestoreFields.COLLECTION_TRUCKS)
            .addSnapshotListener { snapshots, e ->

                if (e != null) {
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }

                val trucks = snapshots?.documents?.mapNotNull { doc ->
                    doc.toFoodTruck()
                } ?: emptyList()

                trySend(Result.success(trucks))
            }

        awaitClose { listener.remove() }
    }

}