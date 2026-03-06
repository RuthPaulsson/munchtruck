package com.example.munchtruck.ui.start

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

// ====== Start Screen (Logic Layer) ===============================

@Composable
fun StartScreen(navController: NavController) {

    // ====== UI Rendering ===============================

    StartContent(
        onOwnerClick = {
            navController.navigate("login")
        },
        onLoverClick = {
            navController.navigate("discovery")
        }
    )
}