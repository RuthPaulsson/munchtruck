package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun register(email: String, password: String) {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val uid = authResult.user?.uid ?: throw Exception("User not found")

        val user = with(FirestoreFields)
        {
            hashMapOf(
                ID to uid,
                EMAIL to email,
                COMPANY_NAME to "",
                ROLE to "owner"
            )
        }
        val truck = mutableMapOf<String, Any?>(
            FirestoreFields.NAME to "",
            FirestoreFields.DESCRIPTION to "",
            FirestoreFields.LOCATION to null,
            FirestoreFields.IMAGE_URL to "",
            FirestoreFields.FOOD_TYPE to "",
            FirestoreFields.HOURS to null
        )

        firestore.runBatch { batch ->
            val userRef = firestore.collection(FirestoreCollections.USERS).document(uid)
            val truckRef = firestore.collection(FirestoreCollections.TRUCKS).document(uid)

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

    override suspend fun reauthenticate(password: String) {
        val user = firebaseAuth.currentUser ?: throw Exception("No user logged in")
        val email = user.email ?: throw Exception("User email is missing.")

        val credential = EmailAuthProvider.getCredential(email, password)
        user.reauthenticate(credential).await()
    }


    override suspend fun deleteUserDocument() {
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("UID is missing")
        firestore.collection("users").document(uid).delete().await()
    }


    override suspend fun deleteAuthAccount() {
        val user = firebaseAuth.currentUser ?: throw Exception("No active session")
        try {
            user.delete().await()
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            throw e
        } catch (e: Exception) {
            throw Exception("No active session ${e.message}")
        }
    }
}