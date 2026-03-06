package com.example.munchtruck.data

import com.google.firebase.firestore.FirebaseFirestoreException

sealed class FirebaseExceptions(message: String, cause: Throwable? = null) : Exception(message, cause) {

    // ====== AUTH ===============================

    class Unauthorized : FirebaseExceptions("User is not authenticated")
    class UserNotFound : FirebaseExceptions("User not found in authentication system")
    class EmailAlreadyInUse : FirebaseExceptions("The email address is already in use")
    class EmailMissing : FirebaseExceptions("The email address is missing")
    class WeakPassword : FirebaseExceptions("The password is too weak")
    class InvalidCredentials : FirebaseExceptions("Invalid email or password")
    class RecentLoginRequired : FirebaseExceptions("Re-authentication is required for this sensitive operation")

    // ====== GENERAL ===============================

    class NotFound : FirebaseExceptions("The requested document or resource was not found")
    class AccessDenied : FirebaseExceptions("Permission denied")
    class ParseError : FirebaseExceptions("Data format is incorrect or mandatory fields are missing")

    // ====== TECH DETAILS ===============================

    data class UploadFailed(val technicalDetails: String?, val origin: Throwable? = null) :
        FirebaseExceptions("Failed to upload image: ${technicalDetails ?: "Unknown cause"}", origin)

    data class StorageError(val technicalDetails: String?, val origin: Throwable? = null) :
        FirebaseExceptions("Storage operation failed: ${technicalDetails ?: "No details"}", origin)

    data class DatabaseError(val technicalDetails: String?, val origin: Throwable? = null) :
        FirebaseExceptions("Database error: ${technicalDetails ?: "No details"}", origin)

    data class Unknown(val technicalDetails: String?, val origin: Throwable? = null) :
        FirebaseExceptions("Unknown error: ${technicalDetails ?: "No details"}", origin)
}

// ============ REPOSITORY HELPERS ======================================

/**
 * Extension to map Firestore SDK exceptions to internal domain exceptions.
 * Standardizes error handling across all repositories.
 */
fun FirebaseFirestoreException.toFirebaseException(): FirebaseExceptions {
    return when (this.code) {
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> FirebaseExceptions.AccessDenied()
        FirebaseFirestoreException.Code.NOT_FOUND -> FirebaseExceptions.NotFound()
        else -> FirebaseExceptions.DatabaseError(this.message, this)
    }
}