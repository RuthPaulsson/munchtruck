package com.example.munchtruck.ui.profile

import android.Manifest
import android.net.Uri
import android.util.Log
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
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.ui.components.ConfirmationDialog
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.ui.components.updateOpeningHoursState
import com.example.munchtruck.util.MenuItemValidationError
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileError
import com.example.munchtruck.viewmodels.ProfileViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

// ====== Edit Profile Screen (Logic Layer) ===============================

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    locationViewModel: LocationViewModel,
    menuViewModel: MenuViewModel
) {
    // ====== State & Initialization ===============================

    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val locationState by locationViewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by menuViewModel.uiState.collectAsStateWithLifecycle()

    val profileErrorMessage = (uiState.error as? ProfileError)?.toMessage()
    val menuErrorMessage = (menuUiState.error as? MenuItemValidationError)?.toMessage()

    val cameraPositionState = rememberCameraPositionState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val existingImageUrl by remember(uiState.imageUrl) { mutableStateOf(uiState.imageUrl) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var menuItemToDelete by remember { mutableStateOf<String?>(null) }
    val deletedMessage = stringResource(R.string.dish_deleted)

    // ====== Side Effects ===============================

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        menuViewModel.observeMenu()
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            navController.popBackStack()
            profileViewModel.resetSaveStatus()
        }
    }

    LaunchedEffect(uiState.isAccountDeleted) {
        if (uiState.isAccountDeleted) {
            navController.navigate("start") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.error) {
        profileErrorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            profileViewModel.clearError()
        }
    }

    LaunchedEffect(menuUiState.error) {
        menuErrorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            menuViewModel.clearError()
        }
    }

    LaunchedEffect(locationState.selectedLat, locationState.selectedLng) {
        val lat = locationState.selectedLat
        val lng = locationState.selectedLng

        if (lat != null && lng != null) {
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f)
                )
            } catch (e: Exception) {
                Log.e("EditProfile", "Map not ready yet")
            }
        }
    }

    // ====== Launchers & Callbacks ===============================

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        locationViewModel.onPermissionResult(isGranted)
        if (isGranted) {
            locationViewModel.useCurrentLocation()
        }
    }

    // ====== UI Rendering ===============================

    EditProfileContent(
        name = uiState.name,
        description = uiState.description,
        foodType = uiState.foodType,
        selectedImageUri = selectedImageUri,
        existingImageUrl = existingImageUrl,
        locationState = locationState,
        cameraPositionState = cameraPositionState,
        menuItems = menuUiState.menuItems,
        isLoading = uiState.isLoading,
        errorMessage = profileErrorMessage,
        openingHours = uiState.openingHours ?: OpeningHours(),
        onNameChange = { profileViewModel.onNameChanged(it) },
        onOpeningHoursChange = { day, interval ->
            val updatedHours = updateOpeningHoursState(
                uiState.openingHours ?: OpeningHours(),
                day,
                interval
            )
            profileViewModel.onOpeningHoursChanged(updatedHours)
        },
        onDescriptionChange = { profileViewModel.onDescriptionChanged(it) },
        onFoodTypeChange = { profileViewModel.onFoodTypeChanged(it) },
        onMapPicked = { lat, lng -> locationViewModel.onMapPicked(lat, lng) },
        onUseCurrentLocation = {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        onSaveLocation = { /* Handled in onSaveClick */ },
        onBackClick = { navController.popBackStack() },
        onSaveClick = {
            locationViewModel.saveLocation()
            profileViewModel.saveProfile(
                name = uiState.name,
                description = uiState.description,
                foodType = uiState.foodType,
                imageUri = selectedImageUri,
                openingHours = uiState.openingHours
            )
        },
        onImageClick = { imageLauncher.launch("image/*") },
        onMenuClick = {
            menuViewModel.resetItemInput()
            navController.navigate("edit_menu/new")
        },
        onEditMenuClick = { id ->
            navController.navigate("edit_menu/$id")
        },
        onDeleteMenuClick = { id ->
            menuItemToDelete = id
            showDeleteDialog = true
        },
        onDeleteAccountClick = {
            profileViewModel.onDeleteAccountClicked()
        },
        isDeleting = uiState.isDeleting,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    )

    // ====== Dialogs (Logic Layer) ===============================

    ConfirmationDialog(
        show = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            showDeleteDialog = false
            menuItemToDelete?.let { id ->
                menuViewModel.deleteMenuItem(id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(deletedMessage)
                }
            }
            menuItemToDelete = null
        },
        title = stringResource(R.string.delete_title),
        message = stringResource(R.string.delete_message),
        isDangerous = true
    )

    ConfirmationDialog(
        show = uiState.showDeleteConfirmation,
        onDismiss = { profileViewModel.onDeleteDismissed() },
        onConfirm = { profileViewModel.onDeleteConfirmed() },
        title = stringResource(R.string.delete_account_title),
        message = stringResource(R.string.delete_account_message),
        confirmText = stringResource(R.string.delete_account_confirm),
        isDangerous = true
    )
}