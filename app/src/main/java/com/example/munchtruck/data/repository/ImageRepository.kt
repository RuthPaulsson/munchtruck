package com.example.munchtruck.data.repository

import android.net.Uri

// ====== Image Repository Interface ===============================
interface ImageRepository {

    // ====== Upload Operations ===============================
    suspend fun uploadProfileImage (imageUri: Uri): String

    suspend fun uploadMenuImage (itemId: String, imageUri: Uri) : String

    // ====== Retrieval Operations ===============================
    suspend fun getProfileImageUri(): String?
}
