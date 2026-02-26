package com.example.munchtruck.data.repository

interface AuthRepository {

    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String)

    fun logout()

    fun isUserLoggedIn(): Boolean
}
