package com.example.munchtruck.data.firebase

import android.net.Uri
import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirebaseStoragePaths
import com.example.munchtruck.data.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageImageRepository(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ImageRepository {

    // ============ ID ==============================================

    private fun truckId(): String =
        auth.currentUser?.uid ?: throw FirebaseExceptions.Unauthorized()


    // ============ PROFILE IMAGE ===========================================

    override suspend fun uploadProfileImage(imageUri: Uri): String {
        return try {

            val path = FirebaseStoragePaths.profilePicture(truckId())
            val ref = storage.getReference(path)

            ref.putFile(imageUri).await()
            ref.downloadUrl.await().toString()

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseExceptions -> e
                else -> FirebaseExceptions.UploadFailed(e.message, e)
            }
        }
    }

    override suspend fun getProfileImageUri(): String? {
        val truckId = auth.currentUser?.uid ?: return null

        return try {
            val path = FirebaseStoragePaths.profilePicture(truckId)
            storage.getReference(path).downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }

    // ============ MENU IMAGE ==============================================

    override suspend fun uploadMenuImage(
        itemId: String,
        imageUri: Uri
    ): String {
        return try {
            val path = FirebaseStoragePaths.menuPicture(truckId(), itemId)
            val ref = storage.getReference(path)

            ref.putFile(imageUri).await()
            ref.downloadUrl.await().toString()

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseExceptions -> e
                else -> FirebaseExceptions.UploadFailed(e.message, e)
            }

        }
    }
}