package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.*
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await



class FirebaseProfileRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepository {

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

    private fun myTruckDoc() =
        firestore.collection(FirestoreFields.COLLECTION_TRUCKS).document(truckId())

    // ============ STATUS & PROFILE ========================================

    override suspend fun updateActiveStatus(isActive: Boolean) {
        // Updates the manual Online/Offline flag in Firestore
        myTruckDoc().update(FirestoreFields.IS_ACTIVE, isActive).await()
    }

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

        if (imageUrl.isNotBlank()) updates[FirestoreFields.IMAGE_URL] = imageUrl.trim()

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
        val weeklyMap = mapOf(
            "mon" to hours.weekly.mon?.toFirestoreMap(),
            "tue" to hours.weekly.tue?.toFirestoreMap(),
            "wed" to hours.weekly.wed?.toFirestoreMap(),
            "thu" to hours.weekly.thu?.toFirestoreMap(),
            "fri" to hours.weekly.fri?.toFirestoreMap(),
            "sat" to hours.weekly.sat?.toFirestoreMap(),
            "sun" to hours.weekly.sun?.toFirestoreMap()
        )

        val hoursData = mapOf(
            FirestoreFields.HOURS to mapOf(
                FirestoreFields.TIME_ZONE to hours.timeZone.ifBlank { "Europe/Stockholm" },
                FirestoreFields.WEEKLY to weeklyMap,
                FirestoreFields.TEMP_CLOSED to hours.tempClosed,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        )
        myTruckDoc().set(hoursData, SetOptions.merge()).await()
    }

    // ============ GET PROFILE (Fetches raw data for UI/ViewModel) ============

    override suspend fun getTruckProfile(): FoodTruck {
        val doc = myTruckDoc().get().await()
        if (!doc.exists()) throw IllegalArgumentException("Profile missing in database")

        val isActive = doc.getBoolean(FirestoreFields.IS_ACTIVE) ?: false

        val locMap = doc.get(FirestoreFields.LOCATION) as? Map<*, *>
        val location = locMap?.let {
            TruckLocation(
                latitude = (it["latitude"] as? Number)?.toDouble() ?: 0.0,
                longitude = (it["longitude"] as? Number)?.toDouble() ?: 0.0,
                address = (it["address"] as? String).orEmpty()
            )
        }

        val hoursMap = doc.get(FirestoreFields.HOURS) as? Map<*, *>
        val weeklyMap = hoursMap?.get(FirestoreFields.WEEKLY) as? Map<*, *>

        val openingHours = OpeningHours(
            timeZone = hoursMap?.get(FirestoreFields.TIME_ZONE) as? String ?: "Europe/Stockholm",
            tempClosed = hoursMap?.get(FirestoreFields.TEMP_CLOSED) as? Boolean ?: false,
            weekly = WeeklyOpeningHours(
                mon = (weeklyMap?.get("mon") as? Map<*, *>)?.toInterval(),
                tue = (weeklyMap?.get("tue") as? Map<*, *>)?.toInterval(),
                wed = (weeklyMap?.get("wed") as? Map<*, *>)?.toInterval(),
                thu = (weeklyMap?.get("thu") as? Map<*, *>)?.toInterval(),
                fri = (weeklyMap?.get("fri") as? Map<*, *>)?.toInterval(),
                sat = (weeklyMap?.get("sat") as? Map<*, *>)?.toInterval(),
                sun = (weeklyMap?.get("sun") as? Map<*, *>)?.toInterval()
            )
        )

        return FoodTruck(
            id = doc.id,
            name = doc.getString(FirestoreFields.NAME).orEmpty(),
            description = doc.getString(FirestoreFields.DESCRIPTION).orEmpty(),
            foodType = doc.getString(FirestoreFields.FOOD_TYPE).orEmpty(),
            imageUrl = doc.getString(FirestoreFields.IMAGE_URL).orEmpty(),
            location = location,
            isOpen = isActive,
            openingHours = openingHours
        )
    }

    // ============ HELPERS =============================================

    private fun OpeningInterval.toFirestoreMap() = mapOf("start" to start, "end" to end)

    private fun Map<*, *>.toInterval(): OpeningInterval? {
        val s = this["start"] as? String ?: return null
        val e = this["end"] as? String ?: return null
        return OpeningInterval(s, e)
    }
}