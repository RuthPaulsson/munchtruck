package com.example.munchtruck.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.ImageRepository
import com.example.munchtruck.data.repository.MenuRepository
import com.example.munchtruck.util.MenuItemValidationError
import com.example.munchtruck.util.MenuItemValidator
import com.example.munchtruck.util.PriceFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ====== Menu State Definitions ===============================

sealed class MenuError {
    data object LoadMenuFailed : MenuError()
    data object SaveItemFailed : MenuError()
    data object DeleteItemFailed : MenuError()
}
data class MenuUiState(
    val itemName: String = "",
    val itemPrice: String = "",
    val itemDescription: String = "",
    val priceError: MenuItemValidationError? = null,
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: MenuError? = null,
    val saveSuccess: Boolean = false
)

// ====== Menu ViewModel ===============================

class MenuViewModel(
    private val repository: MenuRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    // ====== State & Initialization ===============================

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    private var isObserving = false

// ====== Input Validation & Changes ===============================

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(itemName = newName) }
    }

    fun onDescriptionChanged(newDesc: String) {
        _uiState.update { it.copy(itemDescription = newDesc) }
    }

    fun onPriceChanged(newPrice: String) {
        val filtered = newPrice.filter { it.isDigit() || it == ',' || it == '.' }
        if (filtered.count { it == ',' || it == '.' } > 1) return


        _uiState.update { it.copy(itemPrice = filtered, priceError = null) }
    }

    // ====== Menu Data Actions ===============================

    fun saveItem(itemId: String?, imageUri: Uri?) {
        val currentState = _uiState.value


        val nameError = MenuItemValidator.validateName(currentState.itemName)
        val priceError = MenuItemValidator.validatePrice(currentState.itemPrice)
        val descError = MenuItemValidator.validateDescription(currentState.itemDescription)


        val firstError = nameError ?: priceError ?: descError

        if (firstError != null) {
            _uiState.update { it.copy(priceError = firstError) }
            return
        }

        val priceInOre = PriceFormatter.parseToOre(currentState.itemPrice) ?: return


        if (itemId == null) {
            addMenuItem(
                name = currentState.itemName,
                price = priceInOre,
                description = currentState.itemDescription,
                imageUri = imageUri
            )
        } else {
            updateMenuItem(
                itemId = itemId,
                name = currentState.itemName,
                price = priceInOre,
                description = currentState.itemDescription,
                imageUri = imageUri
            )
        }
    }

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

                val existingItem = _uiState.value.menuItems.find { it.id == itemId }

                val imageUrl = if (imageUri != null) {
                    imageRepository.uploadMenuImage(itemId, imageUri)
                } else {
                    existingItem?.imageUrl ?: ""
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

    // ====== UI State Helpers ===============================

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