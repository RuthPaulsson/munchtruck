package com.example.munchtruck.ui.owner

sealed class LocationError {
    object NoPermission : LocationError()
    object NoLocation : LocationError()
    object InvalidCoordinates : LocationError()
    object GpsTimeout : LocationError()
    object AddressTooShort : LocationError()
    object SaveFailed : LocationError()
    data class Unknown(val message: String) : LocationError()
}