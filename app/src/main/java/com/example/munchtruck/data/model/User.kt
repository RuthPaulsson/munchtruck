package com.example.munchtruck.data.model

/**
 * User profile to be stored in Firestore.
 *
 *
 * @property uid Unique identifier for the user (FirebaseAuth UID).
 * @property email Email address used for authentication.
 * @property companyName Company/FoodTruck name associated with the user.
 * @property role User role (e.g., "owner"). Can be used later if introduced to allow more roles.
 */
data class User(
    val uid: String = "",
    val email: String = "",
    val companyName: String = "",
    val role: String = ""
)
