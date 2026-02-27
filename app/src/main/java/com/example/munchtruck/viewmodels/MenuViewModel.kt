package com.example.munchtruck.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.ImageRepository
import com.example.munchtruck.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MenuError {
    data object LoadMenuFailed : MenuError()
    data object SaveItemFailed : MenuError()
    data object DeleteItemFailed : MenuError()
}
data class MenuUiState(
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: MenuError? = null,
    val saveSuccess: Boolean = false
)
class MenuViewModel(
    private val repository: MenuRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    private var isObserving = false

    fun observeMenu() {
        if (isObserving) return
        isObserving = true

        viewModelScope.launch {
            repository.observeMyMenu().collect { result ->
                result.onSuccess { items ->
                    _uiState.update {
                        it.copy(menuItems = items)
                    }
                }.onFailure { error ->
                    isObserving = false
                }
            }
        }
    }

    fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            try {

                _uiState.update { it.copy(isLoading = true, saveSuccess = false) }

                val itemId = repository.addMenuItem(
                    name,
                    price,
                    description,
                    imageUrl = ""
                )

                if (imageUri != null) {
                    val imageUrl = imageRepository.uploadMenuImage(itemId, imageUri)

                    repository.updateMenuItem(itemId, name, price, description, imageUrl)

                }

                _uiState.update { it.copy(isLoading = false, saveSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = MenuError.SaveItemFailed) }
            }
        }
    }

    fun updateMenuItem(
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, saveSuccess = false) }


                val imageUrl = if (imageUri != null) {
                    imageRepository.uploadMenuImage(itemId, imageUri)
                } else {
                    ""
                }

                repository.updateMenuItem(itemId, name, price, description, imageUrl)

                _uiState.update { it.copy(isLoading = false, saveSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = MenuError.SaveItemFailed) }
            }
        }
    }

    fun deleteMenuItem(itemId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMenuItem(itemId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = MenuError.DeleteItemFailed) }
            }
        }
    }

    fun resetState() {
        _uiState.value = MenuUiState()
        isObserving = false
    }
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}