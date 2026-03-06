package com.example.munchtruck.data

object FirebaseStoragePaths {
    private const val ROOT_TRUCKS = "foodtrucks"
    private const val MENU_FOLDER = "menu"

    fun profilePicture(uid: String) = "$ROOT_TRUCKS/$uid/profile.jpg"

    fun menuPicture(uid: String, itemId: String) = "$ROOT_TRUCKS/$uid/$MENU_FOLDER/$itemId.jpg"
}

object FirestoreCollections {
    const val TRUCKS = "foodTrucks"
    const val USERS = "users"
    const val MAP_TRUCKS = "mapTrucks"
    const val MENU = "menu"
}

object FirestoreFields {

    //======= REGULAR FIELDS ==================================

    const val ID = "id"
    const val EMAIL = "email"
    const val COMPANY_NAME = "companyName"
    const val ROLE = "role"
    const val ROLE_OWNER = "owner"
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