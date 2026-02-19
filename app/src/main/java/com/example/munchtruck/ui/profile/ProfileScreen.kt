package com.example.munchtruck.ui.profile

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.viewmodels.AuthViewModel

// ====== Profile Screen ===============================
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
){

    // ====== UI State ===============================

    var showDialog by remember { mutableStateOf(false) }

    ProfileContent(
        isOwner = true,
        truckName = "Crazy Burgers", // change to viewmodel data later
        description = "Best smash burgers in town.", // change to viewmodel data later
        onEditClick = { navController.navigate("edit_profile") },
        onLogoutClick  = { showDialog = true }
    )



    // ====== Logout Dialog ===============================

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false },
            title = { Text(stringResource(R.string.profile_logout_title)) },
            text = { Text(stringResource(R.string.profile_logout_message))},
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.logout()
                    showDialog = false

                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }) {
                    Text(stringResource(R.string.profile_logout_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text(stringResource(R.string.profile_logout_cancel))
                }
            }
        )
    }
}
