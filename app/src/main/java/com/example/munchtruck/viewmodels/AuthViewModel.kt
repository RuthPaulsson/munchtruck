package com.example.munchtruck.viewmodels

import android.util.Log
import com.example.munchtruck.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.util.ValidationResult
import com.example.munchtruck.util.Validators
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for authentication UI state and actions.
 *
 * Exposes:
 * - isLoading: indicates an ongoing auth operation
 * - error: a message to display to the user on validation/auth failure
 * - isLoggedIn: indicates whether the owner is authenticated
 */
class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String> = _error


//  private val _isLoggedIn = MutableStateFlow(repository.isUserLoggedIn()) // todo lägg till när vi har en logout ut knapp

    private val _isLoggedIn =
        MutableStateFlow(false)  // todo ta bort till när vi har en logout knapp
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    /**
     * Attempts to sign in using email and password.
     *
     * @param email User input email.
     * @param password User input password.
     * @return Unit. Updates StateFlow as needed to reflect effects.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = ""

            when (val validate = Validators.validateLogin(email, password)) {
                is ValidationResult.Invalid -> {
                    _error.value = validate.message
                    Log.e("AuthViewModel", "Validation failed: ${validate.message}")
                    return@launch
                }

                ValidationResult.Valid -> Unit
            }

            val trimmedEmail = email.trim()
            val trimmedPassword = password.trim()


            _isLoading.value = true
            try {
                repository.login(trimmedEmail, trimmedPassword)
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Login failed"
                Log.e("AuthViewModel", "Login failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Attempts to register a new user and store initial data in Firestore.
     *
     * @param email User input email.
     * @param password User input password.
     * @param confirmPassword User input password confirmation.
     * @return Unit. Updates StateFlow as needed to reflect effects.
     */
    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {

            _error.value = ""

            when (val validate = Validators.validateRegister(email, password, confirmPassword)) {
                is ValidationResult.Invalid -> {
                    _error.value = validate.message
                    Log.e("AuthViewModel", "Validation failed: ${validate.message}")
                    return@launch
                }

                ValidationResult.Valid -> Unit
            }

            val trimmedEmail = email.trim()
            val trimmedPassword = password.trim()


            _isLoading.value = true
            try {
                repository.register(trimmedEmail, trimmedPassword)
                _isLoggedIn.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Registration failed"
                Log.e("AuthViewModel", "Registration failed: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Signs out the current user and clears authentication-related UI state.
     *
     * @return Unit. After logout, isLoggedIn becomes false and error/loading are reset.
     */
    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
        _error.value = ""
        _isLoading.value = false
    }
}

// Vill man vara inloggad tills man aktivt väljer att logga ut?