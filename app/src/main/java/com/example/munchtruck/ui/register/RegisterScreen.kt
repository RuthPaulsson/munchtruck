package com.example.munchtruck.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.munchtruck.ui.components.toMessage
import com.example.munchtruck.viewmodels.AuthViewModel

// ====== Register Screen (Logic Layer) ===============================

@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // ====== State & Initialization ===============================

    var companyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val errorState by authViewModel.error.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    val errorMessage = errorState?.toMessage() ?: ""

    // ====== Side Effects ===============================

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("profile") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    // ====== UI Rendering ===============================

    RegisterContent(
        company = companyName,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        error = errorMessage,
        isLoading = isLoading,
        onCompanyChange = { companyName = it },
        onEmailChange = { email = it },
        onPasswordChange = { password = it },
        onConfirmPasswordChange = { confirmPassword = it },
        onRegisterClick = {
            authViewModel.register(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                companyName = companyName
            )
        }
    )
}