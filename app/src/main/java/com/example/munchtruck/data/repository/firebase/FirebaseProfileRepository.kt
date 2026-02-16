package com.example.munchtruck.data.repository.firebase


import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseProfileRepository (
    private val firestore: FirebaseFirestore,
) : ProfileRepository {

    private val foodTrucks = firestore.collection("foodTrucks")


    override suspend fun getTruckProfile(ownerUid: String): FoodTruck {
        val snapshot = foodTrucks.document(ownerUid).get().await()
        if (!snapshot.exists()) throw IllegalArgumentException ("FoodTruck profile is missing")

        return FoodTruck (
            id = snapshot.id,
            name = snapshot.getString("name").orEmpty(),
            describtion = snapshot.getString("description").orEmpty(),
            location = snapshot.getString("location").orEmpty(),
            imageUrl = snapshot.getString("imageUrl").orEmpty(),
            isOpen = snapshot.getBoolean("isOpen") ?: false,
            ownerId = snapshot.getString("ownerId").orEmpty()
            /**
            todo
             foodType = snapshot.getString("foodType").orEmpty()
             Kommer vi behöva ha denna ??
             As a food truck owner, I want to edit my profile with name,
              image, description, !!-> and type of food <-!!,
               so that customers can understand what my food truck offers.

            todo : Tror att vi kan slåihop dessa då allt sparas i uid i Foodtruck collectionen,
               vilket ger dem separata id'n och vi hämtar ju bara alla foodtrucks i närheten behöver vi spara
               owner id separat ?
               ownerId = snapshot.getString("ownerId").orEmpty()
               id = snapshot.id,
             */

        )
    }

    override suspend fun updateTruckProfile(
        ownerUid: String,
        name: String,
        description: String,
        location: String,
        isOpen: Boolean,
        imageUrl: String
    ): FoodTruck {
        TODO("Not yet implemented")
    }


}