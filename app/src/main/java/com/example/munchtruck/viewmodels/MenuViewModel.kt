package com.example.munchtruck.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.data.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuUiState(
    val menuItems: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)
class MenuViewModel(
    private val repository: MenuRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    private var isObserving = false

 fun observeMenu(){
     if (isObserving) return
     isObserving = true

     viewModelScope.launch {
            repository.observeMenu().collect { items ->
                _uiState.update {
                    it.copy(menuItems = items)
                }
            }
        }
    }

    fun addMenuItem(
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ){
        viewModelScope.launch {
            try {
                repository.addMenuItem(name, price, description, imageUrl)
                _uiState.update { it.copy(saveSuccess = true) }

            } catch (e: Exception){
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun updateMenuItem(
        itemId: String,
        name: String,
        price: Long,
        description: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            try {
                repository.updateMenuItem(itemId, name, price, description, imageUrl)
                _uiState.update { it.copy(saveSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun deleteMenuItem(itemId: String) {
        viewModelScope.launch {
            try {
                repository.deleteMenuItem(itemId)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

}