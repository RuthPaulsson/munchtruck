package com.example.munchtruck.ui.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.munchtruck.viewmodels.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    EditProfileContent(
        onBackClick = {
            navController.popBackStack()
        },
        onSaveClick = {
            // TODO: connect to viewmodel later
        },
        onImageClick = {
            // TODO: connect later
        }
    )
}
