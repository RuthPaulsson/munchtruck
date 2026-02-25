package com.example.munchtruck.util

sealed class LoginValidationError {
    data object EmptyFields : LoginValidationError()
    data object InvalidEmail : LoginValidationError()
    data object PasswordTooShort : LoginValidationError()
    data object PasswordsDoNotMatch : LoginValidationError()
}
sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: LoginValidationError) : ValidationResult()
}

object Validators {

    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    fun validateLogin (email: String, password: String): ValidationResult {
        val trimmedEmail = email.trim()
        val trimmedPassword = password.trim()

        if (trimmedEmail.isBlank() || trimmedPassword.isBlank()) {
            return ValidationResult.Invalid(LoginValidationError.EmptyFields)
        }

        if (!trimmedEmail.matches(EMAIL_REGEX)) {
            return ValidationResult.Invalid(LoginValidationError.InvalidEmail)
        }

        if (trimmedPassword.length < 6) {
            return ValidationResult.Invalid(LoginValidationError.PasswordTooShort)
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
            return ValidationResult.Invalid(LoginValidationError.EmptyFields)
        }

        if (trimmedPassword != trimmedConfirmPassword) {
            return ValidationResult.Invalid(LoginValidationError.PasswordsDoNotMatch)
        }
        return ValidationResult.Valid
    }
}