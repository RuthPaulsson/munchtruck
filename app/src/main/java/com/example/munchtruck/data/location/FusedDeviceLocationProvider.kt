package com.example.munchtruck.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

// ====== Device Location Provider Implementation ===============================
class FusedDeviceLocationProvider(
    private val context: Context
) : DeviceLocationProvider {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    // ====== Location Logic ===============================
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLatLng(): Pair<Double, Double> {
        if (!hasLocationPermission()) {
            throw SecurityException("Location permission not granted")
        }

        return try {
            val cancellationTokenSource = CancellationTokenSource()
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()

            location?.let {
                it.latitude to it.longitude
            } ?: throw IllegalStateException("Could not get current location")
        } catch (e: SecurityException) {
            throw SecurityException("Location permission denied")
        }
    }
    // ====== Permission Helper ===============================
    private fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    // ====== Geocoding Logic ===============================
   override suspend fun getAddressFromCords(lat: Double, lng: Double): String {
        return withContext(Dispatchers.IO) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lng, 1) // see why .getFromLocation is deprecated

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]

                    val street = address.thoroughfare ?: ""
                    val number = address.subThoroughfare ?: ""

                    if (street.isNotEmpty()) "$street $number".trim() else "Unknown Street"
                } else {
                    "Adress Missing"
                }
            } catch (e: Exception) {
                "Couldn't fetch the adress"
            }
        }
    }
}
