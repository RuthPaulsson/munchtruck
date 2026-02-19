package com.example.munchtruck.util
import com.example.munchtruck.data.location.LocationError

class LocationValidator {

    fun validateCoordinates(lat: Double?, lng: Double?): LocationError? {
        if (lat == null || lng == null) {
            return LocationError.NoLocation
        }

        if (lat < LocationConstants.MIN_LATITUDE || lat > LocationConstants.MAX_LATITUDE) {
            return LocationError.InvalidCoordinates
        }

        if (lng < LocationConstants.MIN_LONGITUDE || lng > LocationConstants.MAX_LONGITUDE) {
            return LocationError.InvalidCoordinates
        }

        if (lat == LocationConstants.DEFAULT_COORDINATE &&
            lng == LocationConstants.DEFAULT_COORDINATE) {
            return LocationError.InvalidCoordinates
        }

        return null
    }

    fun validateAddress(address: String): LocationError? {
        return if (address.length < LocationConstants.MIN_ADDRESS_LENGTH) {
            LocationError.AddressTooShort
        } else {
            null
        }
    }
}