package com.example.munchtruck.data.firebase

import android.net.Uri
import com.example.munchtruck.data.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class StorageImageRepository(
    private val storage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ImageRepository {
    override suspend fun uploadProfileImage(imageUri: Uri): String {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("Ej inloggad")

        val ref = storage.getReference("foodtrucks/$uid/profile.jpg")

        ref.putFile(imageUri).await()
        return ref.downloadUrl.await().toString()
    }

    override suspend fun getProfileImageUri(): String? {
        val uid = auth.currentUser?.uid ?: return null
        val ref = storage.getReference("foodtrucks/$uid/profile.jpg")

        return try {
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}