package com.example.munchtruck.data.repository.firebase


import com.example.munchtruck.data.model.FoodTruck
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


    private fun truckDoc() =
        firestore.collection("foodTrucks")
        .document(truckId())


    override suspend fun getTruckProfile(): FoodTruck {
        val doc = truckDoc().get().await()
        if (!doc.exists()) throw IllegalArgumentException ("FoodTruck profile is missing")

        return FoodTruck (
//            id = doc.id,
//            name = doc.getString("name").orEmpty(),
//            description = doc.getString("description").orEmpty(),
//            location = doc.getString("location").orEmpty(),
//            imageUrl = doc.getString("imageUrl").orEmpty(),
//            isOpen = doc.getBoolean("isOpen") ?: false,
//            foodType = doc.getString("foodType").orEmpty()
        )
    }

    override suspend fun updateTruckProfile(
        name: String,
        description: String,
        location: String,
        imageUrl: String,
        isOpen: Boolean,
        foodType: String
    ): FoodTruck {

        val truckUpdates = mutableMapOf<String, Any>(
            "name" to name.trim(),
            "description" to description.trim(),
            "location" to location.trim(),
            "foodType" to foodType.trim(),
            "isOpen" to isOpen
        )
        if (imageUrl.trim().isNotBlank()) truckUpdates["imageUrl"] = imageUrl.trim()

        truckDoc().set(truckUpdates,SetOptions.merge()).await()
        return getTruckProfile()
    }

}