package com.example.munchtruck.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    locationViewModel: LocationViewModel,
    menuViewModel: MenuViewModel
) {
    // ====== State from ViewModel ===============================

    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val locationState by locationViewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by menuViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        menuViewModel.observeMenu()
    }

    // ====== Local UI State ===============================

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var name by remember(uiState.name) {
        mutableStateOf(uiState.name)
    }

    var description by remember(uiState.description) {
        mutableStateOf(uiState.description)
    }

    var foodType by remember { mutableStateOf("") }


    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuItemToDelete by remember { mutableStateOf<String?>(null) }

    val deletedMessage = stringResource(R.string.dish_deleted)

    // ====== Handle Save Success ===============================

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
            profileViewModel.resetSaveStatus()
        }
    }

    // ====== Handle Error ===============================
    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            snackbarHostState.showSnackbar(message)
            profileViewModel.clearError()
        }
    }

    // ====== Image Picker Launcher ===============================

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    // ====== UI Content ===============================

    EditProfileContent(
        name = name,
        description = description,
        foodType = foodType,
        selectedImageUri = selectedImageUri,
        locationState = locationState,
        menuItems = menuUiState.menuItems,
        onNameChange = { name = it },
        onDescriptionChange = { description = it },
        onFoodTypeChange = { foodType = it },
        onMapPicked = { lat, lng ->
            locationViewModel.onMapPicked(lat, lng)
        },
        onUseCurrentLocation = {
            locationViewModel.useCurrentLocation()
        },
        onSaveLocation = {
            locationViewModel.saveLocation()
        },
        onBackClick = {
            navController.popBackStack()
        },
        onSaveClick = {
            profileViewModel.saveProfile(
                name = name,
                description = description,
                foodType = foodType,
                imageUri = selectedImageUri
            )
        },
        onImageClick = {
            imageLauncher.launch("image/*")
        },
        onMenuClick = {
            navController.navigate("edit_menu/new")
        },
        onEditMenuClick = { id ->
            navController.navigate("edit_menu/$id")
        },

        onDeleteMenuClick = { id ->
            menuViewModel.deleteMenuItem(id)
            showDeleteDialog = true
        },

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

    )


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_title)) },
            text = { Text(stringResource(R.string.delete_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false

                        menuItemToDelete?.let { id ->
                            menuViewModel.deleteMenuItem(id)

                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(deletedMessage)
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete_confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(stringResource(R.string.profile_logout_cancel))
                }
            }
        )
    }
}
