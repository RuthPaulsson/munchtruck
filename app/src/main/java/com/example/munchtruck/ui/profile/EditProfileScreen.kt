package com.example.munchtruck.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.munchtruck.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    // ====== UI State ===============================
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var foodType by remember { mutableStateOf("") }

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
            // TODO: connect to viewmodel later
        },
        onImageClick = {
            imageLauncher.launch("image/*")
        }
    )
}
