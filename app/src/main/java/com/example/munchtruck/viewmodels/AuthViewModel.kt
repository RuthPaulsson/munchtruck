package com.example.munchtruck.viewmodels

import com.example.munchtruck.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.util.ValidationResult
import com.example.munchtruck.util.Validators
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

   // private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String>("")
    val error: StateFlow<String> = _error

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _error.value = ""

            when ( val validate = Validators.validateLogin(email, password)) {
                is ValidationResult.Invalid -> {
                    _error.value = validate.message
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
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {

            _error.value = ""

            when (val validate = Validators.validateRegister(email,password, confirmPassword)) {
                is ValidationResult.Invalid -> {
                    _error.value = validate.message
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
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        repository.logout()
        _isLoggedIn.value = false
    }
}
