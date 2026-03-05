package com.example.munchtruck.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.model.isCurrentlyOpen
import com.example.munchtruck.data.model.isValidInterval
import com.example.munchtruck.data.repository.AuthRepository
import com.example.munchtruck.data.repository.ImageRepository
import com.example.munchtruck.data.repository.ProfileRepository
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update

// ====== Profile State Definitions ===============================

sealed class ProfileError {
    data object LoadProfileFailed : ProfileError()
    data object UpdateFailed : ProfileError()
    data object SignOutFailed : ProfileError()
    data object EmptyName : ProfileError()
    data object InvalidTimeInterval : ProfileError()
    data object DeleteFailed : ProfileError()
    data object RecentLoginRequired : ProfileError()
}
data class ProfileUiState(
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false, // ny kod
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val isAccountDeleted: Boolean = false,
    val error: ProfileError? = null,
    val saveSuccess: Boolean = false,
    val name: String = "",
    val description: String = "",
    val foodType: String = "",
    val imageUrl: String = "",
    val openingHours: OpeningHours? = null,
    val isOpenNow: Boolean = false,

)

    // ====== Profile ViewModel ===============================

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val imageRepository: ImageRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // ====== State & Initialization ===============================

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // ====== Profile Actions ===============================

    fun saveProfile(
        name: String,
        description: String,
        foodType: String,
        imageUri: Uri?,
        openingHours: OpeningHours?
    ) {

        if (name.isBlank()) {
            _uiState.update { it.copy(error = ProfileError.EmptyName) }
            return
        }

        if (openingHours != null) {
            val schedule = openingHours.weekly
            val activeDays = listOfNotNull(
                schedule.mon, schedule.tue, schedule.wed,
                schedule.thu, schedule.fri, schedule.sat, schedule.sun
            )
            if (activeDays.any { !isValidInterval(it.start, it.end) }) {
                _uiState.update { it.copy(error = ProfileError.InvalidTimeInterval) }
                return
            }
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, saveSuccess = false) }

            try {
                val imageUrl: String = if (imageUri != null) {
                    imageRepository.uploadProfileImage(imageUri)
                } else {
                    _uiState.value.imageUrl
                }

                profileRepository.saveMyTruckProfile(
                    name,
                    description,
                    foodType,
                    imageUrl,
                    openingHours = openingHours
                    )

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
        if (uiState.value.isLoaded) return // ny kod

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val truck = profileRepository.getTruckProfile()
                val openStatus = truck.openingHours?.isCurrentlyOpen() ?: false
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoaded = true, // ny kod
                        name = truck.name,
                        description = truck.description,
                        foodType = truck.foodType,
                        imageUrl = truck.imageUrl,
                        openingHours = truck.openingHours,
                        isOpenNow = openStatus
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoaded = true, // ny kod
                        error = ProfileError.LoadProfileFailed
                    )
                }
            }
        }
    }

    // ======= NY kod Ruth ========
    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName, isLoaded = true) }
    }

    fun onDescriptionChanged(newDesc: String) {
        _uiState.update { it.copy(description = newDesc, isLoaded = true) }
    }

    fun onFoodTypeChanged(newType: String) {
        _uiState.update { it.copy(foodType = newType, isLoaded = true) }
    }

    fun onOpeningHoursChanged(newHours: OpeningHours) {
        _uiState.update { it.copy(openingHours = newHours, isLoaded = true) }
    }
    // ====== Account Deletion Flow ===============================

    fun onDeleteAccountClicked() {
        _uiState.update { it.copy(showDeleteConfirmation = true) }
    }
    fun onDeleteDismissed() {
        _uiState.update { it.copy(showDeleteConfirmation = false) }
    }
    fun onDeleteConfirmed() {
        _uiState.update { it.copy(showDeleteConfirmation = false, isDeleting = true, error = null) }

        viewModelScope.launch {
            try {
                profileRepository.deleteAllTruckData()
                authRepository.deleteUserDocument()
                authRepository.deleteAuthAccount()
                _uiState.value = ProfileUiState(isAccountDeleted = true)
            } catch (e: FirebaseAuthRecentLoginRequiredException) {
                _uiState.update { it.copy(
                    isDeleting = false,
                    error = ProfileError.RecentLoginRequired
                ) }
            }
        }
    }



    // ====== UI State Helpers ===============================

    fun resetState(){
        _uiState.value = ProfileUiState()
    }
    fun clearError() = _uiState.update { it.copy(error = null) }
    fun resetSaveStatus() = _uiState.update { it.copy(saveSuccess = false) }
}