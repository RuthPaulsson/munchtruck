package com.example.munchtruck.data

import com.example.munchtruck.data.model.*
import com.google.firebase.firestore.DocumentSnapshot
object FirestoreFields {
    const val COLLECTION_TRUCKS = "foodTrucks"
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val FOOD_TYPE = "foodType"
    const val IMAGE_URL = "imageUrl"
    const val LOCATION = "location"
    const val HOURS = "hours"
    const val WEEKLY = "weekly"
    const val TEMP_CLOSED = "tempClosed"
    const val TIME_ZONE = "timeZone"
}

fun DocumentSnapshot.toFoodTruck(): FoodTruck {
    val locMap = get(FirestoreFields.LOCATION) as? Map<*, *>
    val hoursMap = get(FirestoreFields.HOURS) as? Map<*, *>
    val weeklyMap = hoursMap?.get(FirestoreFields.WEEKLY) as? Map<*, *>

    return FoodTruck(
        id = id,
        name = getString(FirestoreFields.NAME).orEmpty(),
        description = getString(FirestoreFields.DESCRIPTION).orEmpty(),
        foodType = getString(FirestoreFields.FOOD_TYPE).orEmpty(),
        imageUrl = getString(FirestoreFields.IMAGE_URL).orEmpty(),
        location = locMap?.let {
            TruckLocation(
                latitude = (it["latitude"] as? Number)?.toDouble() ?: 0.0,
                longitude = (it["longitude"] as? Number)?.toDouble() ?: 0.0,
                address = (it["address"] as? String).orEmpty(),
                updatedAt = (it["updatedAt"] as? Number)?.toLong() ?: 0L
            )
        },
        openingHours = hoursMap?.let {
            OpeningHours(
                timeZone = it[FirestoreFields.TIME_ZONE] as? String ?: "Europe/Stockholm",
                tempClosed = it[FirestoreFields.TEMP_CLOSED] as? Boolean ?: false,
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
        }
    )
}

fun OpeningInterval.toFirestoreMap() = mapOf("start" to start, "end" to end)

fun Map<*, *>.toInterval(): OpeningInterval? {
    val s = this["start"] as? String ?: return null
    val e = this["end"] as? String ?: return null
    return OpeningInterval(s, e)
}
