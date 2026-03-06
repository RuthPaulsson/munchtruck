package com.example.munchtruck.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.ConfirmationDialog
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileError
import com.example.munchtruck.viewmodels.ProfileViewModel

// ====== Profile Screen (Logic Layer) ===============================

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    menuViewModel: MenuViewModel,
    locationViewModel: LocationViewModel
) {
    // ====== State & Initialization ===============================

    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by menuViewModel.uiState.collectAsStateWithLifecycle()
    val locationUiState by locationViewModel.uiState.collectAsStateWithLifecycle()

    val profileErrorMessage = (uiState.error as? ProfileError)?.toMessage()
    var showLogoutDialog by remember { mutableStateOf(false) }

    // ====== Side Effects ===============================

    LaunchedEffect(Unit) {
        menuViewModel.observeMenu()
        profileViewModel.loadProfile()
    }

    // ====== UI Rendering ===============================

    ProfileContent(
        isOwner = true,
        truckName = uiState.name,
        foodType = uiState.foodType,
        description = uiState.description,
        imageUrl = uiState.imageUrl,
        menuItems = menuUiState.menuItems,
        isLoading = uiState.isLoading,
        errorMessage = profileErrorMessage,
        onEditClick = {
            navController.navigate("edit_profile")
        },
        onLogoutClick = {
            showLogoutDialog = true
        },
        rating = null,
        location = locationUiState.address,
        openingHours = uiState.openingHours
    )

    // ====== Dialogs (Logic Layer) ===============================

    ConfirmationDialog(
        show = showLogoutDialog,
        onDismiss = { showLogoutDialog = false },
        onConfirm = {
            showLogoutDialog = false
            authViewModel.logout()
            navController.navigate("start") {
                popUpTo(0) { inclusive = true }
            }
        },
        title = stringResource(R.string.profile_logout_title),
        message = stringResource(R.string.profile_logout_message),
        confirmText = stringResource(R.string.profile_logout_confirm),
        dismissText = stringResource(R.string.profile_logout_cancel),
        isDangerous = false
    )
}