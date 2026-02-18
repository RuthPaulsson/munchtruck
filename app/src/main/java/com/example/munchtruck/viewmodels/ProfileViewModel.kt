package com.example.munchtruck.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false,
    val name: String = "",
    val description: String = ""
)

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun saveProfile(
        name: String,
        description: String,
        foodType: String,
        imageUri: Uri?
    ) {

        if (name.isBlank()) {
            _uiState.update { it.copy(error = "Name cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, saveSuccess = false) }

            try {
                repository.saveProfile(name, description, foodType, imageUri)

                _uiState.update {
                    it.copy(isSaving = false, saveSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSaving = false, error = e.localizedMessage ?: "Failed to update profile")
                }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(error = null) }
    fun resetSaveStatus() = _uiState.update { it.copy(saveSuccess = false) }

}