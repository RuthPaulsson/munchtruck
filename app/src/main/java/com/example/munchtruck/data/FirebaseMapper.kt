package com.example.munchtruck.data

import com.example.munchtruck.data.model.*
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toFoodTruck(): FoodTruck? {
    val name = getString(FirestoreFields.NAME) ?: return null


    val locMap = get(FirestoreFields.LOCATION) as? Map<*, *>
    val truckLocation = if (locMap != null) {
        TruckLocation(
            latitude = (locMap[FirestoreFields.KEY_LATITUDE] as? Number)?.toDouble() ?: 0.0,
            longitude = (locMap[FirestoreFields.KEY_LONGITUDE] as? Number)?.toDouble() ?: 0.0,
            address = (locMap[FirestoreFields.KEY_ADDRESS] as? String).orEmpty(),
            updatedAt = (locMap[FirestoreFields.UPDATED_AT] as? Number)?.toLong() ?: 0L
        )
    } else {
        TruckLocation(0.0, 0.0, "")
    }


    val hoursMap = get(FirestoreFields.HOURS) as? Map<*, *>
    val weeklyMap = hoursMap?.get(FirestoreFields.WEEKLY) as? Map<*, *>

    val openingHours = if (hoursMap != null) {
        OpeningHours(
            timeZone = hoursMap[FirestoreFields.TIME_ZONE] as? String ?: FirestoreFields.DEFAULT_TIMEZONE,
            tempClosed = hoursMap[FirestoreFields.TEMP_CLOSED] as? Boolean ?: false,
            weekly = WeeklyOpeningHours(
                mon = (weeklyMap?.get(FirestoreFields.DAY_MON) as? Map<*, *>)?.toInterval(),
                tue = (weeklyMap?.get(FirestoreFields.DAY_TUE) as? Map<*, *>)?.toInterval(),
                wed = (weeklyMap?.get(FirestoreFields.DAY_WED) as? Map<*, *>)?.toInterval(),
                thu = (weeklyMap?.get(FirestoreFields.DAY_THU) as? Map<*, *>)?.toInterval(),
                fri = (weeklyMap?.get(FirestoreFields.DAY_FRI) as? Map<*, *>)?.toInterval(),
                sat = (weeklyMap?.get(FirestoreFields.DAY_SAT) as? Map<*, *>)?.toInterval(),
                sun = (weeklyMap?.get(FirestoreFields.DAY_SUN) as? Map<*, *>)?.toInterval()
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

fun OpeningInterval.toFirestoreMap() = mapOf(FirestoreFields.KEY_START to start,
        FirestoreFields.KEY_END to end)

fun Map<*, *>.toInterval(): OpeningInterval? {
    val s = this[FirestoreFields.KEY_START] as? String ?: return null
    val e = this[FirestoreFields.KEY_END] as? String ?: return null
    if (s.isBlank() || e.isBlank()) return null
    return OpeningInterval(s, e)
}

fun DocumentSnapshot.toMenuItem() = MenuItem(
    id = id,
    name = getString(FirestoreFields.NAME).orEmpty(),
    price = getLong(FirestoreFields.PRICE) ?: 0L,
    description = getString(FirestoreFields.DESCRIPTION).orEmpty(),
    imageUrl = getString(FirestoreFields.IMAGE_URL).orEmpty(),
    createdAt = getTimestamp(FirestoreFields.CREATED_AT),
    updatedAt = getTimestamp(FirestoreFields.UPDATED_AT)
)
