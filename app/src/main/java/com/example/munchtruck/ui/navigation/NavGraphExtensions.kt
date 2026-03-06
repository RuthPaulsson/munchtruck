package com.example.munchtruck.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.munchtruck.ui.discovery.DiscoveryScreen
import com.example.munchtruck.ui.forgot.ForgotPasswordScreen
import com.example.munchtruck.ui.login.LoginScreen
import com.example.munchtruck.ui.map.MapScreen
import com.example.munchtruck.ui.menu.EditMenuScreen
import com.example.munchtruck.ui.profile.EditProfileScreen
import com.example.munchtruck.ui.profile.ProfileScreen
import com.example.munchtruck.ui.profile.PublicProfileScreen
import com.example.munchtruck.ui.register.RegisterScreen
import com.example.munchtruck.ui.start.StartScreen
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.DiscoveryViewModel
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileViewModel

// ====== Auth Navigation Graph (Routing Layer) ===============================

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    composable("start") {
        StartScreen(navController)
    }

    composable("login") {
        LoginScreen(navController, authViewModel)
    }

    composable("register") {
        RegisterScreen(navController, authViewModel)
    }

    composable("forgot_password") {
        ForgotPasswordScreen(navController, authViewModel)
    }
}

// ====== Owner Navigation Graph (Routing Layer) ===============================

fun NavGraphBuilder.ownerNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    locationViewModel: LocationViewModel,
    menuViewModel: MenuViewModel
) {
    composable("profile") {
        ProfileScreen(
            navController = navController,
            authViewModel = authViewModel,
            profileViewModel = profileViewModel,
            menuViewModel = menuViewModel,
            locationViewModel = locationViewModel
        )
    }

    composable("edit_profile") {
        EditProfileScreen(
            navController = navController,
            profileViewModel = profileViewModel,
            locationViewModel = locationViewModel,
            menuViewModel = menuViewModel,
            authViewModel = authViewModel
        )
    }

    composable("edit_menu/{itemId}") { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId")
        EditMenuScreen(
            navController = navController,
            viewModel = menuViewModel,
            itemId = if (itemId == "new") null else itemId
        )
    }
}

// ====== Customer Navigation Graph (Routing Layer) ===============================

fun NavGraphBuilder.customerNavGraph(
    navController: NavHostController,
    discoveryViewModel: DiscoveryViewModel
) {
    composable("discovery") {
        DiscoveryScreen(navController, discoveryViewModel) { truckId ->
            navController.navigate("public_profile/$truckId")
        }
    }

    composable("map_view") {
        MapScreen(
            viewModel = discoveryViewModel,
            onTruckClick = { truckId ->
                navController.navigate("public_profile/$truckId")
            },
            onNavigateToHome = {
                navController.navigate("discovery") {
                    popUpTo("discovery") { inclusive = true }
                }
            }
        )
    }

    composable("public_profile/{truckId}") { backStackEntry ->
        val truckId = backStackEntry.arguments?.getString("truckId")
        truckId?.let {
            PublicProfileScreen(
                truckId = it,
                discoveryViewModel = discoveryViewModel,
                navController = navController
            )
        }
    }
}