package com.example.munchtruck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.munchtruck.ui.login.LoginScreen
import com.example.munchtruck.ui.profile.ProfileScreen
import com.example.munchtruck.ui.register.RegisterScreen
import com.example.munchtruck.ui.start.StartScreen
import com.example.munchtruck.viewmodels.AuthViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "start"
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
            ProfileScreen()
        }
    }
}