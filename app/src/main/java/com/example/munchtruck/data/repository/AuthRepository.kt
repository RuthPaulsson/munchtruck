package com.example.munchtruck.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun register(email: String, password: String) {
        try {

        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("User not found")

        val user = hashMapOf(
            "uid" to uid,
            "email" to email
        )

        firestore.collection(
            "users")
            .document(uid)
            .set(user)
            .await()

        } catch (e: Exception) {
            throw e
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}