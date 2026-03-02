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

fun DocumentSnapshot.toFoodTruck(): FoodTruck? {
    val name = getString(FirestoreFields.NAME) ?: return null


    val locMap = get(FirestoreFields.LOCATION) as? Map<*, *>
    val truckLocation = if (locMap != null) {
        TruckLocation(
            latitude = (locMap["latitude"] as? Number)?.toDouble() ?: 0.0,
            longitude = (locMap["longitude"] as? Number)?.toDouble() ?: 0.0,
            address = (locMap["address"] as? String).orEmpty(),
            updatedAt = (locMap["updatedAt"] as? Number)?.toLong() ?: 0L
        )
    } else {
        TruckLocation(0.0, 0.0, "Ingen adress angiven")
    }


    val hoursMap = get(FirestoreFields.HOURS) as? Map<*, *>
    val weeklyMap = hoursMap?.get(FirestoreFields.WEEKLY) as? Map<*, *>

    val openingHours = if (hoursMap != null) {
        OpeningHours(
            timeZone = hoursMap[FirestoreFields.TIME_ZONE] as? String ?: "Europe/Stockholm",
            tempClosed = hoursMap[FirestoreFields.TEMP_CLOSED] as? Boolean ?: false,
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
    } else null

    return FoodTruck(
        id = id,
        name = name,
        description = getString(FirestoreFields.DESCRIPTION).orEmpty(),
        foodType = getString(FirestoreFields.FOOD_TYPE).orEmpty(),
        imageUrl = getString(FirestoreFields.IMAGE_URL).orEmpty(),
        location = truckLocation,
        openingHours = openingHours
    )
}

fun OpeningInterval.toFirestoreMap() = mapOf("start" to start, "end" to end)

fun Map<*, *>.toInterval(): OpeningInterval? {
    val s = this["start"] as? String ?: return null
    val e = this["end"] as? String ?: return null
    if (s.isBlank() || e.isBlank()) return null
    return OpeningInterval(s, e)
}

fun DocumentSnapshot.toMenuItem() = MenuItem(
    id = id,
    name = getString("name").orEmpty(),
    price = getLong("price") ?: 0L,
    description = getString("description").orEmpty(),
    imageUrl = getString("imageUrl").orEmpty(),
    createdAt = getTimestamp("createdAt"),
    updatedAt = getTimestamp("updatedAt")
)
