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


    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }


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

    fun logout() {
        firebaseAuth.signOut()
    }


    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}