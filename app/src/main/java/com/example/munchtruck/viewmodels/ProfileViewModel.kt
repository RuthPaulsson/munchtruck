package com.example.munchtruck.viewmodels


import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel : ViewModel() {
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    fun saveProfile(
        name: String,
        description: String,
        foodType: String,
        imageUri: Uri?
    ){
        // TODO: Replace with real save logic later
        _saveSuccess.value = true
    }
}