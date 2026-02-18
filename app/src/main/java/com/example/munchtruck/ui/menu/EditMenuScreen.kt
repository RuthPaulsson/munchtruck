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
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.viewmodels.MenuViewModel

@Composable
fun EditMenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
    itemId: String? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isEditing = itemId != null

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
            viewModel.resetSaveState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    EditMenuContent(
        name = name,
        price = price,
        description = description,
        selectedImageUri = selectedImageUri,
        isEditing = isEditing,
        onNameChange = { name = it },
        onPriceChange = { price = it },
        onDescriptionChange = { description = it },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {
            val priceLong =
                (price.toDoubleOrNull()?.times(100))?.toLong() ?: 0L
            if (isEditing && itemId != null) {
                viewModel.updateMenuItem(
                    itemId = itemId,
                    name = name,
                    price = priceLong,
                    description = description,
                    imageUrl = ""
                )
            } else {
                viewModel.addMenuItem(
                    name = name,
                    price =priceLong,
                    description = description,
                    imageUrl = ""
                )
            }
        } ,
        onDeleteClick = {
            if (itemId != null) {
                viewModel.deleteMenuItem(itemId)
            }
        },
        onImageClick = {
            imageLauncher.launch("image/*")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}