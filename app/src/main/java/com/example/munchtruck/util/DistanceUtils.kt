
package com.example.munchtruck.util

import android.location.Location
import java.util.Locale
import kotlin.math.roundToInt

// ====== Distance Calculation & Formatting ===========================

object DistanceUtils {

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    fun formatDistance(distanceInMeters: Float): String {
        return if (distanceInMeters < 1000) {
            "${distanceInMeters.roundToInt()} m"
        } else {
            val distanceInKm = distanceInMeters / 1000
            String.format(Locale.getDefault(), "%.1f km", distanceInKm)
        }
    }
}
