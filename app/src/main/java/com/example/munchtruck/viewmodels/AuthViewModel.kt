package com.example.munchtruck.viewmodels

import com.example.munchtruck.data.firebase.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.repository.AuthRepository
import com.example.munchtruck.util.LoginValidationError
import com.example.munchtruck.util.ValidationResult
import com.example.munchtruck.util.Validators
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthError {
    data object EmptyFields : AuthError()
    data object InvalidEmail : AuthError()
    data object PasswordTooShort : AuthError()
    data object LoginFailed : AuthError()
    data object RegistrationFailed : AuthError()
    data object PasswordsDoNotMatch : AuthError()
}
class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<AuthError?>(null)
    val error: StateFlow<AuthError?> = _error.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(repository.isUserLoggedIn())

    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

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

    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
        _isLoading.value = false
        _error.value = null
    }
}
