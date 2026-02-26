package com.example.munchtruck.ui.register


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.navigation.NavController
import com.example.munchtruck.ui.components.toMessage

import com.example.munchtruck.viewmodels.AuthViewModel

// ====== Register Screen ===============================

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
){
    // ====== UI State ===============================

    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val errorState by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val errorMessage = errorState?.toMessage() ?: ""

    // ====== Navigation Effects ===============================

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("profile") {
                popUpTo("register") { inclusive = true }
            }
        }
    }


    // ====== UI Content ===============================

    RegisterContent(
        company = company,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        error = errorMessage,
        isLoading = isLoading,
        onCompanyChange = { company = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegisterClick = {
            authViewModel.register(email, password, confirmPassword)
        }
    )
}
