package com.example.munchtruck.data.model

/**
 * User profile data model.
 * Represents an authenticated user and their associated company and role.
 */
data class User(
    val id: String = "",
    val email: String = "",
    val companyName: String = "",
    val role: String = ""
)
