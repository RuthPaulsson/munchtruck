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


    val profileErrorMessage = (uiState.error as? ProfileError)?.toMessage()
    val menuErrorMessage = (menuUiState.error as? MenuItemValidationError)?.toMessage()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        menuViewModel.observeMenu()
    }

    // ====== Local UI State ===============================

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var existingImageUrl by remember(uiState.imageUrl) {
        mutableStateOf(uiState.imageUrl)
    }

    var name by remember(uiState.name) {
        mutableStateOf(uiState.name)
    }

    var description by remember(uiState.description) {
        mutableStateOf(uiState.description)
    }

    var foodType by remember(uiState.foodType) {
        mutableStateOf(uiState.foodType ?: "")
    }

    var openingHours by remember(uiState.openingHours) {
        mutableStateOf(uiState.openingHours ?: OpeningHours())
    }

    val cameraPositionState = rememberCameraPositionState()

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

    LaunchedEffect(uiState.openingHours) {
        uiState.openingHours?.let { savedHours ->
            openingHours = savedHours
        }
    }

    // ====== Delete account ===============================

    LaunchedEffect(uiState.isAccountDeleted) {
        if (uiState.isAccountDeleted) {
            navController.navigate("start") {
                popUpTo(0) { inclusive = true }

            }
        }
    }

    // ====== Handle Error ===============================
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

    // ====== Image Picker Launcher ===============================

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }


    // ====== Permission Launcher for GPS ===============================
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.values.all { it }
        locationViewModel.onPermissionResult(isGranted)

        if (isGranted) {
            // Om användaren godkänner, hämta platsen direkt
            locationViewModel.useCurrentLocation()
        }
    }
    // ====== Location ===============================

    LaunchedEffect(locationState.selectedLat, locationState.selectedLng) {
        val lat = locationState.selectedLat
        val lng = locationState.selectedLng

        if (lat != null && lng != null) {
            try {
                cameraPositionState.animate(
                    com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                        com.google.android.gms.maps.model.LatLng(lat, lng),
                        15f
                    )
                )
            } catch (e: Exception) {
                // Detta hindrar kraschen "CameraUpdateFactory is not initialized"
                android.util.Log.e("EditProfile", "Kartan inte redo än")
            }
        }
    }

    // ====== UI Content ===============================

    EditProfileContent(
        name = name,
        description = description,
        foodType = foodType,
        selectedImageUri = selectedImageUri,
        existingImageUrl = existingImageUrl,
        locationState = locationState,
        cameraPositionState = cameraPositionState,
        menuItems = menuUiState.menuItems,
        isLoading = uiState.isLoading,
        errorMessage = profileErrorMessage,
        onNameChange = { name = it },
        openingHours = openingHours, // Skicka ner tiderna
        onOpeningHoursChange = { day, interval ->
            openingHours = updateOpeningHoursState(openingHours, day, interval)
        },
        onDescriptionChange = { description = it },
        onFoodTypeChange = { foodType = it },
        onMapPicked = { lat, lng ->
            locationViewModel.onMapPicked(lat, lng)
        },
        onUseCurrentLocation = {
            permissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        },
        onSaveLocation = {
        },

        onBackClick = {
            navController.popBackStack()
        },
        onSaveClick = {
            locationViewModel.saveLocation()
            profileViewModel.saveProfile(
                name = name,
                description = description,
                foodType = foodType,
                imageUri = selectedImageUri,
                openingHours = openingHours

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
            menuItemToDelete = id
            showDeleteDialog = true
        },

        onDeleteAccountClick = { profileViewModel.onDeleteAccountClicked() },
        isDeleting = uiState.isDeleting,

        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

    )

    // ====== Alert Dialog ===============================

    ConfirmationDialog(
        show = showDeleteDialog,
        onDismiss = { showDeleteDialog = false },
        onConfirm = {
            showDeleteDialog = false
            menuItemToDelete?.let { id ->
                menuViewModel.deleteMenuItem(id)
                coroutineScope.launch { snackbarHostState.showSnackbar(deletedMessage) }
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
