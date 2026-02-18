package com.example.munchtruck.ui.profile

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
import com.example.munchtruck.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    // ====== State from ViewModel ===============================

    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

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
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // ====== UI Content ===============================

    EditProfileContent(
        name = name,
        description = description,
        foodType = foodType,
        selectedImageUri = selectedImageUri,
        onNameChange = { name = it },
        onDescriptionChange = { description = it },
        onFoodTypeChange = { foodType = it },
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    )
}
