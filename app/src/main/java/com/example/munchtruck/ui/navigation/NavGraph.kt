package com.example.munchtruck.ui.navigation

import NavDependencyProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileViewModel
import com.example.munchtruck.viewmodels.DiscoveryViewModel
import com.example.munchtruck.viewmodels.LocationViewModel



// ====== Main Navigation Graph ===============================

@Composable
fun NavGraph() {

    // ====== State & Initialization ===============================

    val navController = rememberNavController()
    val context = LocalContext.current
    val provider = remember { NavDependencyProvider(context) }

    // ====== ViewModel Initialization ===============================

    val authViewModel: AuthViewModel = viewModel(factory = provider.authFactory)
    val profileViewModel: ProfileViewModel = viewModel(factory = provider.profileFactory)
    val locationViewModel: LocationViewModel = viewModel(factory = provider.locationFactory)
    val menuViewModel: MenuViewModel = viewModel(factory = provider.menuFactory)
    val discoveryViewModel: DiscoveryViewModel = viewModel(factory = provider.discoveryFactory)

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // ====== Global Navigation Logic ===============================

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {

            profileViewModel.resetState()
            locationViewModel.resetLocationForm()
            menuViewModel.resetState()

            navController.navigate("start") {
                popUpTo(0) { inclusive = true }
            }
        } else {
            // Förbered data för inloggad ägare
            profileViewModel.loadProfile()
            menuViewModel.observeMenu()
        }
    }

    // ====== NavHost Configuration ===============================

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "profile" else "start"
    ) {
        // Vi anropar våra extension-funktioner från NavGraphExtensions.kt
        authNavGraph(navController, authViewModel)
        ownerNavGraph(navController, authViewModel, profileViewModel, locationViewModel, menuViewModel)
        customerNavGraph(navController, discoveryViewModel)
    }
}