package com.example.munchtruck.data.repository.firebase

import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseMenuRepository (
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : MenuRepository {

    private fun menuCollection(truckId: String) = firestore.collection(
        "foodTrucks")
        .document(truckId)
        .collection("menu")

    override fun observeMenu(truckId: String): Flow<List<MenuItem>> = callbackFlow {
        val listener = menuCollection(truckId).addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }


            val menuItems = snapshot?.documents.orEmpty().map { doc ->
                MenuItem(
//                    id = doc.id,
//                    name = doc.getString("name").orEmpty(),
//                    price = doc.getLong("price") ?: 0L,
//                    description = doc.getString("description").orEmpty(),
//                    imageUrl = doc.getString("imageUrl").orEmpty()
                )
            }
            trySend(menuItems).isSuccess

        }
        awaitClose { listener.remove() }
    }

    override suspend fun addMenuItem(
        truckId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {

        val trimmedName = name.trim()
        val trimmedDescription = description.trim()
        val trimmedImageUrl = imageUrl.trim()

        require(name.isNotBlank()){"Name cannot be blank"}
        require(price > 0){"Price must be greater than 0"}

        val itemRef = menuCollection(truckId).document()
        val itemData = mutableMapOf<String, Any>(
            "id" to itemRef.id,
            "name" to trimmedName,
            "price" to price,
            "description" to trimmedDescription
        )
        if (trimmedImageUrl.isNotBlank()) itemData ["imageUrl"] = trimmedImageUrl


        itemRef.set(itemData).await()
    }

    override suspend fun updateMenuItem(
        truckId: String,
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {
        require(name.isNotBlank()){"Name cannot be blank"}
        require(price > 0){"Price must be greater than 0"}

        val updateMenuItem = mutableMapOf<String, Any>(
            "name" to name,
            "price" to price,
            "description" to description
        )
        if (imageUrl.isNotBlank()) updateMenuItem["imageUrl"] = imageUrl

        menuCollection(truckId).document(itemId).set(updateMenuItem).await()

    }

    override suspend fun deleteMenuItem(truckId: String, itemId: String) {
        menuCollection(truckId).document(itemId).delete().await()
    }
}