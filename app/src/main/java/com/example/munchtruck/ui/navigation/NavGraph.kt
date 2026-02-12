package com.example.munchtruck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.munchtruck.ui.login.LoginScreen
import com.example.munchtruck.ui.profile.ProfileScreen
import com.example.munchtruck.ui.register.RegisterScreen
import com.example.munchtruck.ui.start.StartScreen
import com.example.munchtruck.viewmodels.AuthViewModel

// ====== Navigation Graph ===============================

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // TEMP: Use start as entry for development until role/onboarding is implemented
    val startDestination = if (isLoggedIn) "profile" else "start"
    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable("start") {
            StartScreen(navController)

        }

        composable("login"){
            LoginScreen(navController,authViewModel)
        }

        composable("register"){
            RegisterScreen(navController,authViewModel )
        }
        composable("profile"){
            if (isLoggedIn) {
                ProfileScreen(navController,authViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login"){
                        popUpTo(0)
                    }
                }
            }

        }
    }
}