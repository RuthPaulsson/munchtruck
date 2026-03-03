package com.example.munchtruck.data


object FirestoreFields {

    //======= REGULAR FIELDS ==================================
    const val NAME = "name"
    const val DESCRIPTION = "description"
    const val FOOD_TYPE = "foodType"
    const val IMAGE_URL = "imageUrl"
    const val PRICE = "price"


    //======== PLACE AND TIME  ==================================


    const val LOCATION = "location"
    const val HOURS = "hours"
    const val WEEKLY = "weekly"
    const val TEMP_CLOSED = "tempClosed"
    const val TIME_ZONE = "timeZone"
    const val DEFAULT_TIMEZONE = "Europe/Stockholm"


    //======== INTERNAL KEYS (MAPS)  ==================================


    const val KEY_LATITUDE = "latitude"
    const val KEY_LONGITUDE = "longitude"
    const val KEY_ADDRESS = "address"
    const val KEY_START = "start"
    const val KEY_END = "end"


    //======== METADATA  =========================================


    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"


    //======== DAYS  =========================================


    const val DAY_MON = "mon"
    const val DAY_TUE = "tue"
    const val DAY_WED = "wed"
    const val DAY_THU = "thu"
    const val DAY_FRI = "fri"
    const val DAY_SAT = "sat"
    const val DAY_SUN = "sun"
}