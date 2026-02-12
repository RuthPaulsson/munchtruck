package com.example.munchtruck.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.munchtruck.viewmodels.AuthViewModel

// ====== Profile Screen ===============================
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
){

    // ====== UI State ===============================

    var showDialog by remember { mutableStateOf(false) }

    // ====== Layout ===============================

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Welcome!")

            Spacer(modifier = Modifier.height((24.dp)))

            Button(onClick = {
                showDialog = true
            }) {
                Text("Log out")
            }
        }
    }

    // ====== Logout Dialog ===============================

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false },
            title = { Text("Log out")},
            text = { Text("Are you sure you want to log out?")},
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.logout()
                    showDialog = false

                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}