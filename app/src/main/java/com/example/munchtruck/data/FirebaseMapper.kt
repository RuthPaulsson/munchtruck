package com.example.munchtruck.data

import com.example.munchtruck.data.model.*
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentSnapshot.toFoodTruck(): FoodTruck? = with(FirestoreFields) {
    val name = getString(NAME) ?: return null


    val locMap = get(LOCATION) as? Map<*, *>
    val truckLocation = if (locMap != null) {
        TruckLocation(
            latitude = (locMap[KEY_LATITUDE] as? Number)?.toDouble() ?: 0.0,
            longitude = (locMap[KEY_LONGITUDE] as? Number)?.toDouble() ?: 0.0,
            address = (locMap[KEY_ADDRESS] as? String).orEmpty(),
            updatedAt = (locMap[UPDATED_AT] as? Number)?.toLong() ?: 0L
        )
    } else {
        TruckLocation(0.0, 0.0, "")
    }


    val hoursMap = get(HOURS) as? Map<*, *>
    val weeklyMap = hoursMap?.get(WEEKLY) as? Map<*, *>

    val openingHours = if (hoursMap != null) {
        OpeningHours(
            timeZone = hoursMap[TIME_ZONE] as? String ?: DEFAULT_TIMEZONE,
            tempClosed = hoursMap[TEMP_CLOSED] as? Boolean ?: false,
            weekly = WeeklyOpeningHours(
                mon = (weeklyMap?.get(DAY_MON) as? Map<*, *>)?.toInterval(),
                tue = (weeklyMap?.get(DAY_TUE) as? Map<*, *>)?.toInterval(),
                wed = (weeklyMap?.get(DAY_WED) as? Map<*, *>)?.toInterval(),
                thu = (weeklyMap?.get(DAY_THU) as? Map<*, *>)?.toInterval(),
                fri = (weeklyMap?.get(DAY_FRI) as? Map<*, *>)?.toInterval(),
                sat = (weeklyMap?.get(DAY_SAT) as? Map<*, *>)?.toInterval(),
                sun = (weeklyMap?.get(DAY_SUN) as? Map<*, *>)?.toInterval()
            )
        )
    } else null

    return FoodTruck(
        id = id,
        name = name,
        description = getString(DESCRIPTION).orEmpty(),
        foodType = getString(FOOD_TYPE).orEmpty(),
        imageUrl = getString(IMAGE_URL).orEmpty(),
        location = truckLocation,
        openingHours = openingHours
    )
}

fun OpeningInterval.toFirestoreMap() = with(FirestoreFields) {
    mapOf(
        KEY_START to start,
        KEY_END to end
    )
}
fun Map<*, *>.toInterval(): OpeningInterval? = with(FirestoreFields) {
    val s = this@toInterval[KEY_START] as? String ?: return null
    val e = this@toInterval[KEY_END] as? String ?: return null
    if (s.isBlank() || e.isBlank()) return null

    OpeningInterval(s, e)
}

fun DocumentSnapshot.toMenuItem() = with(FirestoreFields) {
     MenuItem(
    id = id,
    name = getString(NAME).orEmpty(),
    price = getLong(PRICE) ?: 0L,
    description = getString(DESCRIPTION).orEmpty(),
    imageUrl = getString(IMAGE_URL).orEmpty(),
    createdAt = getTimestamp(CREATED_AT),
    updatedAt = getTimestamp(UPDATED_AT)
)
     }
