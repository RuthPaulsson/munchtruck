package com.example.munchtruck.data.firebase

import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.FirestoreCollections
import com.example.munchtruck.data.FirestoreFields
import com.example.munchtruck.data.repository.AuthRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()

        }catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidUserException -> FirebaseExceptions.UserNotFound()
                is FirebaseAuthInvalidCredentialsException -> FirebaseExceptions.InvalidCredentials()
                else -> FirebaseExceptions.Unknown(e.message, e)
        }   }
    }

    override suspend fun register(email: String, password: String) {
        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw FirebaseExceptions.UserNotFound()

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

        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthUserCollisionException -> FirebaseExceptions.EmailAlreadyInUse()
                is FirebaseAuthWeakPasswordException -> FirebaseExceptions.WeakPassword()
                is FirebaseExceptions -> e
                else -> FirebaseExceptions.Unknown(e.message, e)
            }
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun reauthenticate(password: String) {
        val user = firebaseAuth.currentUser ?: throw FirebaseExceptions.Unauthorized()
        val email = user.email ?: throw FirebaseExceptions.EmailMissing()

        try {
            val credential = EmailAuthProvider.getCredential(email, password)
            user.reauthenticate(credential).await()
        }catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthInvalidCredentialsException -> FirebaseExceptions.InvalidCredentials()
                else -> FirebaseExceptions.Unknown(e.message, e)
            }
        }
    }


    override suspend fun deleteUserDocument() {
        val uid = firebaseAuth.currentUser?.uid ?: throw FirebaseExceptions.Unauthorized()
        try {
            firestore.collection(FirestoreCollections.USERS)
                .document(uid).delete().await()
        } catch (e: Exception) {
            throw FirebaseExceptions.DatabaseError(e.message,e)
        }

    }


    override suspend fun deleteAuthAccount() {
        val user = firebaseAuth.currentUser ?: throw FirebaseExceptions.Unauthorized()
        try {
            user.delete().await()
        } catch (e: Exception) {
            throw when (e) {
                is FirebaseAuthRecentLoginRequiredException -> FirebaseExceptions.RecentLoginRequired()
                else -> FirebaseExceptions.Unknown(e.message, e)
            }
        }
    }
}