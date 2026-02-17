package com.example.munchtruck.data.repository

import android.net.Uri

interface ImageRepository {

    suspend fun uploadProfileImage (imageUri: Uri): String

    suspend fun getProfileImageUri(): String?
}
