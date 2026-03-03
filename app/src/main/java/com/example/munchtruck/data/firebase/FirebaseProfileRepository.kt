package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.*
import com.example.munchtruck.data.repository.ProfileRepository
import com.example.munchtruck.data.toFirestoreMap
import com.example.munchtruck.data.toFoodTruck
import com.example.munchtruck.data.toMenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await


class FirebaseProfileRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ProfileRepository {

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    private fun myTruckDoc() =
        firestore.collection(FirestoreFields.COLLECTION_TRUCKS).document(truckId())

    // ============ STATUS & PROFILE ========================================

    override suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
        openingHours: OpeningHours?
    ) {
        val updates = mutableMapOf<String, Any>(
            FirestoreFields.NAME to name.trim(),
            FirestoreFields.DESCRIPTION to description.trim(),
            FirestoreFields.FOOD_TYPE to foodType.trim()
        )


        openingHours?.let { hours ->
            val weeklyMap = mapOf(
                "mon" to hours.weekly.mon?.toFirestoreMap(),
                "tue" to hours.weekly.tue?.toFirestoreMap(),
                "wed" to hours.weekly.wed?.toFirestoreMap(),
                "thu" to hours.weekly.thu?.toFirestoreMap(),
                "fri" to hours.weekly.fri?.toFirestoreMap(),
                "sat" to hours.weekly.sat?.toFirestoreMap(),
                "sun" to hours.weekly.sun?.toFirestoreMap()
            )

            updates[FirestoreFields.HOURS] = mapOf(
                FirestoreFields.TIME_ZONE to hours.timeZone.ifBlank { "Europe/Stockholm" },
                FirestoreFields.WEEKLY to weeklyMap,
                FirestoreFields.TEMP_CLOSED to hours.tempClosed,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        }

        myTruckDoc().set(updates, SetOptions.merge()).await()
    }

    // ============ LOCATION & HOURS ========================================

    override suspend fun updateMyTruckLocation(location: TruckLocation) {
        val updatedLocation = mapOf(
            FirestoreFields.LOCATION to mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "address" to location.address.trim(),
                "updatedAt" to location.updatedAt
            )
        )
        myTruckDoc().set(updatedLocation, SetOptions.merge()).await()
    }

    override suspend fun updateMyTruckOpeningHours(hours: OpeningHours) {
        saveMyTruckProfile("", "", "", "", hours)
    }

    // ============ GET PROFILE (Fetches raw data for UI/ViewModel) ============

    override suspend fun getTruckProfile(): FoodTruck {
        val doc = myTruckDoc().get().await()
        if (!doc.exists()) throw IllegalArgumentException("Profile is missing in database")


        return doc.toFoodTruck() ?: FoodTruck(
            id = doc.id,
            name = "",
            location = TruckLocation(0.0, 0.0, "")
        )
    }

    override suspend fun deleteAllTruckData() {
        val uid = truckId() // it works but look into if this needs to be here
        val truckDoc = myTruckDoc().get().await()
        val foodTruck = truckDoc.toFoodTruck()

        val menuSnapshot = myTruckDoc().collection(FirestoreFields.COLLECTION_MENU).get().await()
        val menuItems = menuSnapshot.documents.map { it.toMenuItem() }


        foodTruck?.imageUrl?.let { url ->
            deleteImageFile(url)
        }

        menuItems.forEach { item ->
            if (item.imageUrl.isNotBlank()) {
                deleteImageFile(item.imageUrl)
            }
        }

        firestore.runBatch { batch ->
            menuSnapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }
            batch.delete(myTruckDoc())
        }.await()


    }

    private suspend fun deleteImageFile(url: String) {
        if (url.isBlank()) return
        try {
            storage.getReferenceFromUrl(url).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e.message?.contains("Object does not exist") == false) {
                throw e
            }
        }

    }

}