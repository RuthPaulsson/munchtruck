package com.example.munchtruck.data.location

sealed class LocationExceptions(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class PermissionDenied : LocationExceptions("Location permission not granted")
    class ProviderUnavailable : LocationExceptions("Location services are unavailable or disabled")
    class AddressNotFound : LocationExceptions("Could not find an address for these coordinates")

    data class GeocodingFailed(val details: String?, val origin: Throwable? = null) :
        LocationExceptions("Geocoding failed: ${details ?: "No details"}", origin)
}