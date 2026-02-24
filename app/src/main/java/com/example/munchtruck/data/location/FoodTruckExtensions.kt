package com.example.munchtruck.data.location
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.FoodTruckWithDistance
import com.example.munchtruck.util.DistanceUtils

fun FoodTruck.withDistance(userLat: Double, userLon: Double): FoodTruckWithDistance? {
    val location = this.location ?: return null
    val distance = DistanceUtils.calculateDistance(
        lat1 = userLat,
        lon1 = userLon,
        lat2 = location.latitude,
        lon2 = location.longitude
    )
    return FoodTruckWithDistance(foodTruck = this, distanceInMeters = distance)
}

fun FoodTruckWithDistance.getFormattedDistance(): String =
    DistanceUtils.formatDistance(this.distanceInMeters)

fun List<FoodTruckWithDistance>.sortedByNearest(): List<FoodTruckWithDistance> =
    sortedBy { it.distanceInMeters }