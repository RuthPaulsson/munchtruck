package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseMenuRepository (
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MenuRepository {

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")


    private fun menuCollection() = firestore.collection(
        "foodTrucks")
        .document(truckId())
        .collection("menu")


    override fun observeMenu(): Flow<List<MenuItem>> = callbackFlow {
        val listener = menuCollection().addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }


            val menuItems = snapshot?.documents.orEmpty().map { doc ->
                MenuItem(
                    id = doc.id,
                    name = doc.getString("name").orEmpty(),
                    price = doc.getLong("price") ?: 0L,
                    description = doc.getString("description").orEmpty(),
                    imageUrl = doc.getString("imageUrl").orEmpty(),
                    createdAt = doc.getTimestamp("createdAt"),
                    updatedAt = doc.getTimestamp("updatedAt")

                )
            }
            trySend(menuItems)

        }
        awaitClose { listener.remove() }
    }

    override suspend fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) : String {

        val itemRef = menuCollection().document()
        val itemData = mutableMapOf(
            "name" to name.trim(),
            "price" to price,
            "description" to description.trim(),
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (imageUrl.trim().isNotBlank()) itemData ["imageUrl"] = imageUrl.trim()

        itemRef.set(itemData).await()
        return itemRef.id
    }

    override suspend fun updateMenuItem(
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {

        val updatedMenuItem = mutableMapOf(
            "name" to name.trim(),
            "price" to price,
            "description" to description.trim(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (imageUrl.isNotBlank()) updatedMenuItem["imageUrl"] = imageUrl.trim()

        menuCollection().document(itemId).set(updatedMenuItem, SetOptions.merge())
            .await()

    }

    override suspend fun deleteMenuItem(itemId: String) {
        menuCollection().document(itemId).delete().await()
    }
}