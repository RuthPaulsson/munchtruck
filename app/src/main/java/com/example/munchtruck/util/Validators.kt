package com.example.munchtruck.util


sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val message: String) : ValidationResult()
}

object Validators {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    fun validateLogin (email: String, password: String): ValidationResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            return ValidationResult.Invalid("Email and password cannot be empty")
        }

        if (!trimmedEmail.matches(EMAIL_REGEX)) {
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
