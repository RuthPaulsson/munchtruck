package com.example.munchtruck.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Repository responsible for authentication and related Firestore user data.
 *
 * This class acts as the data layer between ViewModels and Firebase services:
 * - FirebaseAuth: sign in / register / sign out
 * - Firestore: store basic user profile data on registration
 */
class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Signs in a user using Firebase Authentication.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @throws Exception If sign-in fails (invalid credentials, network issues, etc.).
     * @return Unit. On success, FirebaseAuth will hold an authenticated session.
     */
    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }


    /**
     * This function creates a new user account in Firebase Authentication with the provided email and password.
     * and stores initial user data in Firestore
     *
     * @param email The new user's email address.
     * @param password The new user's password.
     * @throws Exception If registration fails or UID is missing.
     * @return Unit. On success, user data is written to Firestore.
     */
    suspend fun register(email: String, password: String) {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("User not found")

        val user = hashMapOf(
            "uid" to uid,
            "email" to email,
            "companyName" to "",
            "role" to "owner"
        )

        firestore.collection("users")
            .document(uid)
            .set(user)
            .await()

    }

    /**
     * Signs out the currently authenticated user.
     */
    fun logout() {
        firebaseAuth.signOut()
    }

    /**
     * Checks whether a user is currently authenticated in FirebaseAuth.
     *
     * Returns true if there is an active Firebase session
     */
    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}