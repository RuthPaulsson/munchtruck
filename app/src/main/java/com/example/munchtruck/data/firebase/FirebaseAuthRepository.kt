package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepository: AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }


    override suspend fun register(email: String, password: String) {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("User not found")

        val user = hashMapOf(
            "id" to uid,
            "email" to email,
            "companyName" to "",
            "role" to "owner"
        )

        val truck = mutableMapOf<String, Any>(
            "name" to "",
            "description" to "",
            "location" to "",
            "imageUrl" to "",
            "foodType" to "",
            "openingHours" to ""
        )

        firestore.runBatch { batch ->
            val userRef = firestore.collection("users").document(uid)
            val truckRef = firestore.collection("foodTrucks").document(uid)

            batch.set(userRef, user)
            batch.set(truckRef, truck)
        }.await()


    }

    override fun logout() {
        firebaseAuth.signOut()
    }


    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}