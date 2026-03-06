package com.example.munchtruck.ui.forgot

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
import kotlinx.coroutines.delay

// ====== Forgot Password Screen ===============================

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    // ====== UI State ===============================

    var email by remember { mutableStateOf("") }

    val errorState by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isResetEmailSent by authViewModel.isResetEmailSent.collectAsState()

    val errorMessage = errorState?.toMessage() ?: ""

    // ====== Side Effects ===============================

    LaunchedEffect(Unit) {
        authViewModel.clearError()
        authViewModel.clearResetStatus()
    }

    LaunchedEffect(isResetEmailSent) {
        if (isResetEmailSent) {
            delay(3000)
            navController.popBackStack()
        }
    }

    // ====== UI Content ===============================

    ForgotPasswordContent(
        email = email,
        onEmailChange = { email = it },
        onResetClick = {
            authViewModel.sendPasswordReset(email)
        },
        isLoading = isLoading,
        error = errorMessage,
        isSuccess = isResetEmailSent
    )
}