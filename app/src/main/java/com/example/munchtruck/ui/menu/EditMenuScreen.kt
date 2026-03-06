package com.example.munchtruck.ui.menu

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.MenuViewModel
import kotlinx.coroutines.launch

// ====== Edit Menu Screen (Logic Layer) ===============================

@Composable
fun EditMenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
    itemId: String? = null
) {
    // ====== State & Initialization ===============================

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val isEditing = itemId != null
    val item = uiState.menuItems.find { it.id == itemId }
    val existingImageUrl = item?.imageUrl

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var hasLoadedItem by remember { mutableStateOf(false) }

    // ====== Side Effects ===============================

    LaunchedEffect(Unit) {
        viewModel.observeMenu()
    }

    LaunchedEffect(uiState.menuItems, itemId) {
        if (!hasLoadedItem && isEditing && itemId != null) {
            val currentItem = uiState.menuItems.find { it.id == itemId }
            currentItem?.let {
                viewModel.onNameChanged(it.name)
                viewModel.onDescriptionChanged(it.description)
                viewModel.onPriceChanged((it.price / 100.0).toString())
                hasLoadedItem = true
            }
        }
    }

    val successMessage = stringResource(R.string.menu_item_saved)
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(successMessage)
            }
            navController.popBackStack()
            viewModel.resetSaveState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it.toString())
            viewModel.clearError()
        }
    }

    // ====== Callbacks & Launchers ===============================

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    // ====== UI Rendering ===============================

    EditMenuContent(
        name = uiState.itemName,
        price = uiState.itemPrice,
        description = uiState.itemDescription,
        selectedImageUri = selectedImageUri,
        existingImageUrl = existingImageUrl,
        isLoading = uiState.isLoading,
        priceError = uiState.priceError?.toMessage(),
        onNameChange = { viewModel.onNameChanged(it) },
        onPriceChange = { viewModel.onPriceChanged(it) },
        onDescriptionChange = { viewModel.onDescriptionChanged(it) },
        onBackClick = { navController.popBackStack() },
        onSaveClick = { viewModel.saveItem(itemId, selectedImageUri) },
        onImageClick = { imageLauncher.launch("image/*") },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    )
}