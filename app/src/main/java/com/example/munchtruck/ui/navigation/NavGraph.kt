package com.example.munchtruck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import com.example.munchtruck.ui.login.LoginScreen
import com.example.munchtruck.ui.start.StartScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ){
        composable("start") {
            StartScreen(navController)

        }

        composable("login"){
            LoginScreen(navController)
        }
    }
}