package com.example.munchtruck.util

import android.util.Patterns

/**
 * Represents the result of a validation operation.
 *
 * - [Valid] means the input passed all validation rules.
 * - [Invalid] contains a user-facing error message explaining why validation failed.
 */
sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

/**
 * Utility object responsible for validating authentication-related user input.
 *
 * This class contains pure validation logic and has no dependency on Firebase or any
 * other external services.
 */
object Validators {

    fun validateLogin (email: String, password: String): ValidationResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
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

    fun validateRegister (email: String, password: String, confirmPassword: String): ValidationResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()
        val trimmedConfirmPassword = confirmPassword.trim()

        val validationResult = validateLogin(trimmedEmail, trimmedPassword)
        if (validationResult is ValidationResult.Invalid) {
            return validationResult
        }

        if (trimmedConfirmPassword.isBlank()) {
            return ValidationResult.Invalid("Confirm password cannot be empty")
        }

        if (trimmedPassword != trimmedConfirmPassword) {
            return ValidationResult.Invalid("Passwords do not match")
        }
        return ValidationResult.Valid
    }


}


