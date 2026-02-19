package com.example.munchtruck.data.repository.firebase


import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await


class FirebaseProfileRepository (
    private val firestore: FirebaseFirestore,
    private val auth : FirebaseAuth
) : ProfileRepository {

    fun truckId () : String =
        auth.currentUser?.uid ?: throw IllegalStateException("Ej inloggad")


    private fun myTruckDoc() =
        firestore.collection("foodTrucks")
        .document(truckId())

    override suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
//        isOpen: Boolean
    ) {

        val truckUpdates = mutableMapOf<String, Any>(
            "name" to name.trim(),
            "description" to description.trim(),
            "foodType" to foodType.trim(),
//            "isOpen" to isOpen
        )
        if (imageUrl.trim().isNotBlank()) truckUpdates["imageUrl"] = imageUrl.trim()

        myTruckDoc().set(truckUpdates,SetOptions.merge()).await()
    }

    override suspend fun updateMyTruckLocation(location: TruckLocation) {

        val updatedLocation = mapOf(
            "location" to mapOf(
                "latitude" to location.latitude,
                "longitude" to location.longitude,
                "address" to location.address.trim(),
                "updatedAtMilis" to location.updatedAtMilis
            )
        )
        myTruckDoc().set(updatedLocation, SetOptions.merge()).await()

    }

    override suspend fun getTruckProfile(): FoodTruck {
        val doc = myTruckDoc().get().await()
        if (!doc.exists()) throw IllegalArgumentException ("FoodTruck profile is missing")

        val loc = doc.get("location") as? Map<*, *>
        val lat = (loc?.get("latitude") as? Number)?.toDouble()
        val long = (loc?.get("longitude") as? Number)?.toDouble()

        val location = if (lat != null && long != null){
            TruckLocation(
                latitude = lat,
                longitude = long,
                address = (loc["address"] as? String).orEmpty(),
                updatedAtMilis = (loc["updatedAtMilis"] as? Number)?.toLong() ?: 0L
            )
        } else {
            null
        }

        return FoodTruck (
            id = doc.id,
            name = doc.getString("name").orEmpty(),
            description = doc.getString("description").orEmpty(),
            foodType = doc.getString("foodType").orEmpty(),
            location = location,
            imageUrl = doc.getString("imageUrl").orEmpty(),
//            isOpen = doc.getBoolean("isOpen") ?: false
        )
    }

}