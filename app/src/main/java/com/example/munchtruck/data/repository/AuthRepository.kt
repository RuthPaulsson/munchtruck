package com.example.munchtruck.data.repository

// ====== Authentication Repository Interface ===============================

interface AuthRepository {

    // ====== Session Management ===============================

    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String, trimmedCompanyName: String)

    suspend fun sendPasswordResetEmail(email: String)

    fun logout()

    fun isUserLoggedIn(): Boolean

    // ====== Account Security & Management ===============================
    suspend fun reauthenticate(password: String)

    suspend fun deleteUserDocument()

    suspend fun deleteAuthAccount()
}