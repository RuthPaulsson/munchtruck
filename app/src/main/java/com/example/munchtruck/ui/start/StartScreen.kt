package com.example.munchtruck.ui.start


import androidx.compose.runtime.Composable
import androidx.navigation.NavController


// ====== Start Screen ===============================
@Composable
fun StartScreen(navController: NavController) {

    StartContent(
        onOwnerClick = { navController.navigate("login") },
        onLoverClick = { navController.navigate("discovery") }
    )
}