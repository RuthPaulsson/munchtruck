package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.model.*
import com.example.munchtruck.data.repository.ProfileRepository
import com.example.munchtruck.data.toFirestoreMap
import com.example.munchtruck.data.toFoodTruck
import com.example.munchtruck.data.toMenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

private const val STORAGE_ERROR_NOT_FOUND = "Object does not exist"
class FirebaseProfileRepository(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : ProfileRepository {

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw FirebaseExceptions.Unauthorized()

    private fun myTruckDoc() =
        firestore.collection(FirestoreCollections.TRUCKS).document(truckId())

    // ============ STATUS & PROFILE ========================================

    override suspend fun saveMyTruckProfile(
        name: String,
        description: String,
        foodType: String,
        imageUrl: String,
        openingHours: OpeningHours?
    ) {
        try {
            val updates = with(FirestoreFields) {
                mutableMapOf<String, Any>(
                    NAME to name.trim(),
                    DESCRIPTION to description.trim(),
                    FOOD_TYPE to foodType.trim(),
                    IMAGE_URL to imageUrl
                )

            }

            openingHours?.let { hours ->
                with(FirestoreFields) {
                    val weeklyMap = mapOf(
                        DAY_MON to hours.weekly.mon?.toFirestoreMap(),
                        DAY_TUE to hours.weekly.tue?.toFirestoreMap(),
                        DAY_WED to hours.weekly.wed?.toFirestoreMap(),
                        DAY_THU to hours.weekly.thu?.toFirestoreMap(),
                        DAY_FRI to hours.weekly.fri?.toFirestoreMap(),
                        DAY_SAT to hours.weekly.sat?.toFirestoreMap(),
                        DAY_SUN to hours.weekly.sun?.toFirestoreMap()
                    )

                    updates[FirestoreFields.HOURS] = mapOf(
                        TIME_ZONE to hours.timeZone.ifBlank { DEFAULT_TIMEZONE },
                        WEEKLY to weeklyMap,
                        TEMP_CLOSED to hours.tempClosed,
                        UPDATED_AT to FieldValue.serverTimestamp()
                    )
                }
            }

            myTruckDoc().set(updates, SetOptions.merge()).await()
        } catch (e: Exception) {
            throw FirebaseExceptions.DatabaseError(e.message,e)
        }
    }

    // ============ LOCATION & HOURS ========================================

    override suspend fun updateMyTruckLocation(location: TruckLocation) {
        try {
            val updatedLocation = with(FirestoreFields) {
                mapOf(
                    FirestoreFields.LOCATION to mapOf(
                        KEY_LATITUDE to location.latitude,
                        KEY_LONGITUDE to location.longitude,
                        KEY_ADDRESS to location.address.trim(),
                        UPDATED_AT to location.updatedAt
                    )
                )
            }
            myTruckDoc().set(updatedLocation, SetOptions.merge()).await()
        } catch (e: Exception) {
            throw FirebaseExceptions.DatabaseError(e.message,e)
        }
    }

    override suspend fun updateMyTruckOpeningHours(hours: OpeningHours) {
        saveMyTruckProfile("", "", "", "", hours)
    }

    // ============ GET PROFILE (Fetches raw data for UI/ViewModel) ============


    override suspend fun getTruckProfile(): FoodTruck {
        return try {
            val doc = myTruckDoc().get().await()
            if (!doc.exists()) throw FirebaseExceptions.NotFound()

            doc.toFoodTruck() ?: throw FirebaseExceptions.ParseError()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseExceptions -> e
                else -> FirebaseExceptions.Unknown(e.message, e)
            }
        }
    }



    override suspend fun deleteAllTruckData() {
        try {
            val truckDoc = myTruckDoc().get().await()
            val foodTruck = truckDoc.toFoodTruck()

            val menuSnapshot = with(FirestoreCollections) {
                myTruckDoc().collection(MENU).get().await()
            }
            val menuItems = menuSnapshot.documents.map { it.toMenuItem() }

            foodTruck?.imageUrl?.let { deleteImageFile(it) }
            menuItems.forEach { item ->
                if (item.imageUrl.isNotBlank()) deleteImageFile(item.imageUrl)
            }


            firestore.runBatch { batch ->
                menuSnapshot.documents.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.delete(myTruckDoc())
            }.await()

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthRecentLoginRequiredException -> FirebaseExceptions.RecentLoginRequired()
                else -> FirebaseExceptions.DatabaseError(e.message,e)
            }
        }
    }

    private suspend fun deleteImageFile(url: String) {
        if (url.isBlank()) return
        try {
            storage.getReferenceFromUrl(url).delete().await()
        } catch (e: Exception) {
            if (e.message?.contains(STORAGE_ERROR_NOT_FOUND) == false) {
                throw FirebaseExceptions.StorageError(e.message,e)
            }
        }
    }

}