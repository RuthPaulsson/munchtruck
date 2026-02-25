package com.example.munchtruck.data.firebase


import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.model.OpeningInterval
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.model.WeeklyOpeningHours
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlin.collections.get


class FirebaseProfileRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ProfileRepository {


    // ============ TRUCK ID/DOC =============================================

    fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Ej inloggad")


    private fun myTruckDoc() =
        firestore.collection("foodTrucks")
            .document(truckId())


    // ============ SAVE TRUCK =============================================


    override suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String
    ) {

        val truckUpdates = mutableMapOf<String, Any>(
            "name" to name.trim(),
            "description" to description.trim(),
            "foodType" to foodType.trim()
        )
        if (imageUrl.trim().isNotBlank()) truckUpdates["imageUrl"] = imageUrl.trim()

        myTruckDoc().set(truckUpdates, SetOptions.merge()).await()
    }


    // ============ UPDATE TRUCK =============================================


    override suspend fun updateMyTruckLocation(location: TruckLocation) {

        val updatedLocation = mapOf(
            "location" to mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "address" to location.address.trim(),
                "updatedAt" to location.updatedAt
            )
        )
        myTruckDoc().set(updatedLocation, SetOptions.merge()).await()

    }

    override suspend fun updateMyTruckOpeningHours(hours: OpeningHours) {
        val timezone = hours.timeZone.trim().ifBlank { "Europe/Stockholm" }

        val weeklyMap: Map<String, Any?> = mapOf(
            "mon" to hours.weekly.mon?.toFirestoreMap(),
            "tue" to hours.weekly.tue?.toFirestoreMap(),
            "wed" to hours.weekly.wed?.toFirestoreMap(),
            "thu" to hours.weekly.thu?.toFirestoreMap(),
            "fri" to hours.weekly.fri?.toFirestoreMap(),
            "sat" to hours.weekly.sat?.toFirestoreMap(),
            "sun" to hours.weekly.sun?.toFirestoreMap()
        )

        val hoursMap = mapOf(
            "hours" to mapOf(
                "timeZone" to timezone,
                "weekly" to weeklyMap,
                "tempClosed" to hours.tempClosed,
                "updatedAt" to FieldValue.serverTimestamp()
            )
        )

        myTruckDoc().set(hoursMap, SetOptions.merge()).await()
    }


    // ============ GET TRUCK =============================================


    override suspend fun getTruckProfile(): FoodTruck {
        val doc = myTruckDoc().get().await()
        if (!doc.exists()) throw IllegalArgumentException("FoodTruck profile is missing")

        val loc = doc.get("location") as? Map<*, *>
        val lat = (loc?.get("latitude") as? Number)?.toDouble()
        val long = (loc?.get("longitude") as? Number)?.toDouble()

        val location = if (lat != null && long != null) {
            TruckLocation(
                latitude = lat,
                longitude = long,
                address = (loc["address"] as? String).orEmpty(),
                updatedAt = (loc["updatedAt"] as? Number)?.toLong() ?: 0L
            )
        } else {
            null
        }

        val hoursMap = doc.get("hours") as? Map<*, *>
        val weekly = (hoursMap?.get("weekly") as? Map<*, *>)

        val weeklyHours = WeeklyOpeningHours(
            mon = (weekly?.get("mon") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            tue = (weekly?.get("tue") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            wed = (weekly?.get("wed") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            thu = (weekly?.get("thu") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            fri = (weekly?.get("fri") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            sat = (weekly?.get("sat") as? Map<*, *>)?.toOpeningIntervalOrNull(),
            sun = (weekly?.get("sun") as? Map<*, *>)?.toOpeningIntervalOrNull()
        )

        val openingHours: OpeningHours? =
            if (hoursMap != null) {
                OpeningHours(
                    timeZone = (hoursMap["timeZone"] as? String)?.ifBlank { "Europe/Stockholm" }
                        ?: "Europe/Stockholm",
                    weekly = weeklyHours,
                    tempClosed = hoursMap["tempClosed"] as? Boolean ?: false

                )
            } else {
                null
            }


        return FoodTruck(
            id = doc.id,
            name = doc.getString("name").orEmpty(),
            description = doc.getString("description").orEmpty(),
            foodType = doc.getString("foodType").orEmpty(),
            location = location,
            imageUrl = doc.getString("imageUrl").orEmpty(),
            openingHours = openingHours
        )


    }

    // ============ HELPERS =============================================


    private fun OpeningInterval.toFirestoreMap(): Map<String, Any> {
        return mapOf(
            "start" to start.trim(),
            "end" to end.trim()
        )
    }

    private fun Map<*, *>.toOpeningIntervalOrNull(): OpeningInterval? {
        val start = (this["start"] as? String)?.trim().orEmpty()
        val end = (this["end"] as? String)?.trim().orEmpty()

        if (start.isBlank() || end.isBlank()) return null

        return OpeningInterval(start = start, end = end)
    }

}