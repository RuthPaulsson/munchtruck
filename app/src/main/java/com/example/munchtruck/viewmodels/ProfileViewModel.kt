package com.example.munchtruck.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.repository.ImageRepository
import com.example.munchtruck.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

sealed class ProfileError {
    data object LoadProfileFailed : ProfileError()
    data object UpdateFailed : ProfileError()
    data object SignOutFailed : ProfileError()
    data object EmptyName : ProfileError()
}
data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: ProfileError? = null,
    val saveSuccess: Boolean = false,
    val name: String = "",
    val description: String = "",
    val foodType: String = "",
    val imageUrl: String = "",
    val openingHours: OpeningHours? = null,
    val isOpenNow: Boolean = false
)

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val imageRepository: ImageRepository
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
            _uiState.update { it.copy(error = ProfileError.EmptyName) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, saveSuccess = false) }

            try {
                val imageUrl: String = if (imageUri != null) {
                    imageRepository.uploadProfileImage(imageUri)
                } else ""

                repository.saveMyTruckProfile(name, description, foodType, imageUrl)

                _uiState.update {
                    it.copy(isSaving = false, saveSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        error = ProfileError.UpdateFailed
                    )
                }
            }
        }
    }

    fun loadProfile() {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val truck = repository.getTruckProfile()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = truck.name,
                        description = truck.description,
                        foodType = truck.foodType,
                        imageUrl = truck.imageUrl
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = ProfileError.LoadProfileFailed
                    )
                }
            }
        }

    }

    fun resetState(){
        _uiState.value = ProfileUiState()
    }
    fun clearError() = _uiState.update { it.copy(error = null) }
    fun resetSaveStatus() = _uiState.update { it.copy(saveSuccess = false) }
}