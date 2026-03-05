package com.example.munchtruck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.FirebaseExceptions
import com.example.munchtruck.data.repository.AuthRepository
import com.example.munchtruck.util.LoginValidationError
import com.example.munchtruck.util.ValidationResult
import com.example.munchtruck.util.Validators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

    // ====== Auth State Definitions ===============================

sealed class AuthError {
    data object EmptyFields : AuthError()
    data object InvalidEmail : AuthError()
    data object PasswordTooShort : AuthError()
    data object LoginFailed : AuthError()
    data object RegistrationFailed : AuthError()
    data object PasswordsDoNotMatch : AuthError()
    data object UserNotFound : AuthError()
    data object NetworkError : AuthError()
    data object Unknown : AuthError()
}

    // ====== Auth ViewModel ===============================

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // ====== State & Initialization ===============================

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<AuthError?>(null)
    val error: StateFlow<AuthError?> = _error.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(repository.isUserLoggedIn())

    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isResetEmailSent = MutableStateFlow(false)
    val isResetEmailSent: StateFlow<Boolean> = _isResetEmailSent.asStateFlow()

    // ====== Authentication Actions ===============================

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = null

            when ( val validate = Validators.validateLogin(email, password)) {
                is ValidationResult.Invalid -> {
                    _error.value = when (validate.error) {
                        is LoginValidationError.EmptyFields -> AuthError.EmptyFields
                        is LoginValidationError.InvalidEmail -> AuthError.InvalidEmail
                        is LoginValidationError.PasswordTooShort -> AuthError.PasswordTooShort
                        else -> AuthError.LoginFailed
                    }
                    return@launch
                }
                ValidationResult.Valid -> Unit
        }

            val trimmedEmail = email.trim()
            val trimmedPassword = password.trim()


            _isLoading.value = true
            try {
                repository.login(trimmedEmail, trimmedPassword)
                _isLoggedIn.value = repository.isUserLoggedIn()
            } catch (e: Exception) {
                _error.value = AuthError.LoginFailed
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {

            _error.value = null

            when (val validate = Validators.validateRegister(email,password, confirmPassword)) {
                is ValidationResult.Invalid -> {
                    _error.value = when (validate.error) {
                        is LoginValidationError.EmptyFields -> AuthError.EmptyFields
                        is LoginValidationError.InvalidEmail -> AuthError.InvalidEmail
                        is LoginValidationError.PasswordTooShort -> AuthError.PasswordTooShort
                        is LoginValidationError.PasswordsDoNotMatch -> AuthError.PasswordsDoNotMatch
                        else -> AuthError.RegistrationFailed
                    }
                    return@launch
                }

                ValidationResult.Valid -> Unit
            }

            val trimmedEmail = email.trim()
            val trimmedPassword = password.trim()

            _isLoading.value = true
            try {
                repository.register(trimmedEmail, trimmedPassword)
                _isLoggedIn.value = repository.isUserLoggedIn()
            } catch (e: Exception) {
                _error.value = AuthError.RegistrationFailed
            } finally {
                _isLoading.value = false
            }
        }
    }



    fun sendPasswordReset(email: String) {
        val trimmedEmail = email.trim()

        if (trimmedEmail.isEmpty()) {
            _error.value = AuthError.EmptyFields
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _isResetEmailSent.value = false

            try {
                repository.sendPasswordResetEmail(trimmedEmail)
                _isResetEmailSent.value = true
            } catch (e: Exception) {
                _error.value = when (e) {
                    is FirebaseExceptions.UserNotFound -> AuthError.UserNotFound
                    else -> AuthError.Unknown
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ====== UI State Helpers ===============================

    fun clearError() {
        _error.value = null
    }

    fun clearResetStatus() {
        _isResetEmailSent.value = false
    }
    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
        _isLoading.value = false
        _error.value = null
    }
}
