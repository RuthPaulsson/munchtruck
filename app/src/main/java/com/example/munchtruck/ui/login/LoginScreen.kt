package com.example.munchtruck.ui.login


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.munchtruck.viewmodels.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("profile"){
                popUpTo("login") { inclusive = true }
            }
        }
    }

    LoginContent(
        email = email,
        password = password,
        error = error,
        isLoading = isLoading,
        onEmailChange = { email = it },
        onPasswordChange = { password = it },

        onLoginClick = {
            authViewModel.login(email, password)
        },
        onRegisterClick = {
            navController.navigate("register")
        },
        onForgotPasswordClick = {
            navController.navigate("forgot_password")
        }
    )

}
