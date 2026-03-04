package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.DiscoveryRepository
import com.example.munchtruck.data.toFoodTruck
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseDiscoveryRepository(
    private val firestore: FirebaseFirestore,
) : DiscoveryRepository {

    override fun observeOpenTrucks(): Flow<Result<List<FoodTruck>>> = callbackFlow {

        val listener = with(FirestoreCollections) {
            firestore.collection(TRUCKS)
                .addSnapshotListener { snapshots, e ->

                    if (e != null) {
                        val error = when (e.code) {
                            FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseExceptions.AccessDenied()
                            else -> FirebaseExceptions.DatabaseError(e.message, e)
                        }
                        trySend(Result.failure(error))
                        return@addSnapshotListener
                    }

                    val trucks = snapshots?.documents?.mapNotNull { doc ->
                        doc.toFoodTruck()
                    } ?: emptyList()

                    trySend(Result.success(trucks))
                }
        }
        awaitClose { listener.remove() }
    }
}
