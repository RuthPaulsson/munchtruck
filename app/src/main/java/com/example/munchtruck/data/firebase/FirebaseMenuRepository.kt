package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import com.example.munchtruck.data.toMenuItem
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

    // ============ ID ==============================================

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

    // ============ COLLECTION ==============================================

    private fun myMenuCollection() =
        firestore.collection(FirestoreFields.COLLECTION_TRUCKS)
        .document(truckId()).collection("menu")



    // ============ OBSERVE ==============================================


    override fun observeMyMenu(): Flow<Result<List<MenuItem>>> = callbackFlow {
        val listener = myMenuCollection().addSnapshotListener { snapshot, e ->
            if (e != null) {
                trySend(Result.failure(e))
                return@addSnapshotListener
            }

            val items = snapshot?.documents.orEmpty().map { it.toMenuItem() }

            trySend(Result.success(items))
        }
        awaitClose { listener.remove() }
    }

    override fun observeTruckMenu(truckId: String): Flow<Result<List<MenuItem>>> = callbackFlow {
        val listener = firestore.collection(FirestoreFields.COLLECTION_TRUCKS)
            .document(truckId)
            .collection("menu")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }

                val items = snapshot?.documents.orEmpty().map { it.toMenuItem() }
                trySend(Result.success(items))
            }
        awaitClose { listener.remove() }
    }

    // ============ CREATE ===============================================


    override suspend fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) : String {

        val itemRef = myMenuCollection().document()
        val itemData = mutableMapOf(
            "name" to name.trim(),
            "price" to price,
            "description" to description.trim(),
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (imageUrl.trim().isNotBlank()) itemData ["imageUrl"] = imageUrl.trim()

        itemRef.set(itemData, SetOptions.merge()).await()
        return itemRef.id
    }

    // ============ UPDATE ===============================================


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

        myMenuCollection().document(itemId).set(updatedMenuItem, SetOptions.merge())
            .await()

    }


    // ============ DELETE ===============================================


    override suspend fun deleteMenuItem(itemId: String) {
        myMenuCollection().document(itemId).delete().await()
    }
}