package com.example.munchtruck.util

import android.util.Patterns

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

object Validators {

    fun validateLogin (email: String, password: String): ValidationResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            return ValidationResult.Invalid("Email and password cannot be empty")
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            return ValidationResult.Invalid("Invalid email address")
        }

        if (trimmedPassword.length < 6) {
            return ValidationResult.Invalid("Password must be at least 6 characters long")
        }

        return ValidationResult.Valid
    }


}


