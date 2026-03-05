package com.example.munchtruck.ui.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.ConfirmationDialog
import com.example.munchtruck.ui.components.toDisplayString
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileError
import com.example.munchtruck.viewmodels.ProfileViewModel

// ====== Profile Screen ===============================
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    menuViewModel: MenuViewModel,
    locationViewModel: LocationViewModel
){
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val menuUiState by menuViewModel.uiState.collectAsStateWithLifecycle()
    val locationUiState by locationViewModel.uiState.collectAsStateWithLifecycle()
    val profileErrorMessage = (uiState.error as? ProfileError)?.toMessage()

    LaunchedEffect(Unit) {
        menuViewModel.observeMenu()
        profileViewModel.loadProfile()
    }


    // ====== UI State ===============================

    var showDialog by remember { mutableStateOf(false) }

    ProfileContent(
        isOwner = true,
        truckName = uiState.name,
        foodType = uiState.foodType,
        description = uiState.description,
        imageUrl = uiState.imageUrl,
        menuItems = menuUiState.menuItems,
        isLoading = uiState.isLoading,
        errorMessage = profileErrorMessage,
        onEditClick = { navController.navigate("edit_profile") },
        onLogoutClick  = { showDialog = true },
        rating = null,
        location = locationUiState.address,
        openingHours = uiState.openingHours

    )

    // ====== Logout Dialog ===============================

    ConfirmationDialog(
        show = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = {
            authViewModel.logout()
            showDialog = false
            navController.navigate("login") { popUpTo(0) }
        },
        title = stringResource(R.string.profile_logout_title),
        message = stringResource(R.string.profile_logout_message),
        confirmText = stringResource(R.string.profile_logout_confirm),
        dismissText = stringResource(R.string.profile_logout_cancel),
        isDangerous = false
    )
}

