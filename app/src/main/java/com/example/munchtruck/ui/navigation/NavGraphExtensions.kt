package com.example.munchtruck.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.munchtruck.ui.discovery.DiscoveryScreen
import com.example.munchtruck.ui.login.LoginScreen
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

// ====== Auth Navigation Graph ===============================

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    composable("start") { StartScreen(navController) }
    composable("login") { LoginScreen(navController, authViewModel) }
    composable("register") { RegisterScreen(navController, authViewModel) }
}

// ====== Owner Navigation Graph ===============================

fun NavGraphBuilder.ownerNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    locationViewModel: LocationViewModel,
    menuViewModel: MenuViewModel
) {
    composable("profile") {
        ProfileScreen(navController, authViewModel, profileViewModel, menuViewModel, locationViewModel)
    }
    composable("edit_profile") {
        EditProfileScreen(navController, profileViewModel, locationViewModel, menuViewModel)
    }
    composable("edit_menu/{itemId}") { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId")
        EditMenuScreen(navController, menuViewModel, if (itemId == "new") null else itemId)
    }
}

// ====== Customer Navigation Graph ===============================

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
        com.example.munchtruck.ui.map.MapScreen(discoveryViewModel, { truckId ->
            navController.navigate("public_profile/$truckId")
        }, {
            navController.navigate("discovery") { popUpTo("discovery") { inclusive = true } }
        })
    }
    composable("public_profile/{truckId}") { backStackEntry ->
        val truckId = backStackEntry.arguments?.getString("truckId")
        truckId?.let { PublicProfileScreen(it, discoveryViewModel) }
    }
}