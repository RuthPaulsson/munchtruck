package com.example.munchtruck.data

sealed class FirebaseExceptions (message: String) : Exception(message) {

    class Unauthorized : FirebaseExceptions("User is not authenticated")
    class UserNotFound : FirebaseExceptions("User not found in authentication system")
    class EmailAlreadyInUse : FirebaseExceptions("The email address is already in use")
    class EmailMissing : FirebaseExceptions("The email address is missing")
    class WeakPassword : FirebaseExceptions("The password is too weak")
    class InvalidCredentials : FirebaseExceptions("Invalid email or password")
    class RecentLoginRequired : FirebaseExceptions("Re-authentication is required for this sensitive operation")


    data class DatabaseError(val technicalDetails: String?) :
        FirebaseExceptions("Database error: ${technicalDetails ?: "No details"}")

    data class Unknown(val technicalDetails: String?, val origin: Throwable? = null) :
        FirebaseExceptions("Unknown error: ${technicalDetails ?: "No details"}")
}