package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.model.MapTruck
import com.example.munchtruck.data.repository.MapRepository
import com.example.munchtruck.data.toFoodTruck
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseMapRepository(
    private val firestore: FirebaseFirestore,
) : MapRepository {

    // ============ OBSERVE ==============================================

    override fun observeActiveTrucksForMap(): Flow<Result<List<MapTruck>>> = callbackFlow {

        val listener = with(FirestoreCollections) {
        firestore.collection(MAP_TRUCKS)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    val wrappedError = when (error.code) {
                        FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseExceptions.AccessDenied()
                        else -> FirebaseExceptions.DatabaseError(error.message,error)
                    }
                    trySend(Result.failure(wrappedError))
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
        }
        awaitClose { listener.remove() }
    }
}