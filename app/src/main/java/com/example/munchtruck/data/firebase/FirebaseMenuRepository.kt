package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import com.example.munchtruck.data.toMenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseMenuRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : MenuRepository {

    // ============ ID ==============================================

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw FirebaseExceptions.Unauthorized()

    // ============ COLLECTION ==============================================

    private fun myMenuCollection() = with(FirestoreCollections) {
        firestore.collection(TRUCKS)
            .document(truckId()).collection(MENU)
    }


    // ============ OBSERVE ==============================================


    override fun observeMyMenu(): Flow<Result<List<MenuItem>>> = callbackFlow {
        val listener = myMenuCollection().addSnapshotListener { snapshot, e ->
            if (e != null) {
                val wrappedError = when (e.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseExceptions.AccessDenied()
                    else -> FirebaseExceptions.DatabaseError(e.message,e)
                }
                trySend(Result.failure(wrappedError))
                return@addSnapshotListener
            }

            val items = snapshot?.documents.orEmpty().map { it.toMenuItem() }

            trySend(Result.success(items))
        }
        awaitClose { listener.remove() }
    }

    override fun observeTruckMenu(truckId: String): Flow<Result<List<MenuItem>>> = callbackFlow {
        val listener = with(FirestoreCollections) {
            firestore.collection(TRUCKS)
                .document(truckId)
                .collection(MENU)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        val wrappedError = when (e.code) {
                            FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseExceptions.AccessDenied()
                            else -> FirebaseExceptions.DatabaseError(e.message,e)
                        }
                        trySend(Result.failure(wrappedError))
                        return@addSnapshotListener
                    }

                    val items = snapshot?.documents.orEmpty().map { it.toMenuItem() }
                    trySend(Result.success(items))
                }

        }
        awaitClose { listener.remove() }
    }

    // ============ CREATE ===============================================


    override suspend fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ): String {
        return try {
            val itemRef = myMenuCollection().document()

            val itemData = with(FirestoreFields) {
                mutableMapOf(
                    NAME to name.trim(),
                    PRICE to price,
                    DESCRIPTION to description.trim(),
                    IMAGE_URL to imageUrl,
                    CREATED_AT to FieldValue.serverTimestamp(),
                    UPDATED_AT to FieldValue.serverTimestamp()
                )
            }
            itemRef.set(itemData, SetOptions.merge()).await()
            itemRef.id
        } catch (e: Exception) {

            throw FirebaseExceptions.DatabaseError(e.message,e)
        }

    }

    // ============ UPDATE ===============================================


    override suspend fun updateMenuItem(
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {
        try {
            val updatedMenuItem = with(FirestoreFields) {
                mutableMapOf(
                    NAME to name.trim(),
                    PRICE to price,
                    DESCRIPTION to description.trim(),
                    IMAGE_URL to imageUrl,
                    UPDATED_AT to FieldValue.serverTimestamp()
                )
            }
            myMenuCollection().document(itemId).set(updatedMenuItem, SetOptions.merge()).await()
        } catch (e: Exception) {
            throw FirebaseExceptions.DatabaseError(e.message,e)
        }
    }


    // ============ DELETE ===============================================


    override suspend fun deleteMenuItem(itemId: String) {
        try {
            myMenuCollection().document(itemId).delete().await()
        } catch (e: Exception) {
            throw FirebaseExceptions.DatabaseError(e.message,e)
        }
    }
}