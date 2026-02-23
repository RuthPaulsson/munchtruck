package com.example.munchtruck.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.munchtruck.data.firebase.FirebaseMenuRepository
import com.example.munchtruck.ui.login.LoginScreen
import com.example.munchtruck.ui.menu.EditMenuScreen
import com.example.munchtruck.ui.profile.EditProfileScreen
import com.example.munchtruck.ui.profile.ProfileScreen
import com.example.munchtruck.ui.register.RegisterScreen
import com.example.munchtruck.ui.start.StartScreen
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import androidx.lifecycle.ViewModelProvider
import com.example.munchtruck.data.firebase.FirebaseProfileRepository
import com.example.munchtruck.data.firebase.StorageImageRepository
import com.example.munchtruck.data.location.FusedDeviceLocationProvider
import com.example.munchtruck.viewmodels.LocationViewModel
import com.google.firebase.storage.FirebaseStorage


// ====== Navigation Graph ===============================

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
//    val locationViewModel: LocationViewModel = viewModel()

    val profileRepository = remember {
        FirebaseProfileRepository(
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        )
    }

    val imageRepository = remember {
        StorageImageRepository(
            FirebaseStorage.getInstance(),
            FirebaseAuth.getInstance()
        )
    }
    val profileViewModel: ProfileViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProfileViewModel(profileRepository, imageRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val context = LocalContext.current

    val locationProvider = remember {
        FusedDeviceLocationProvider(context)
    }

    val locationViewModel: LocationViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LocationViewModel(
                        profileRepository,
                        locationProvider
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val menuRepository = remember {
        FirebaseMenuRepository(
            FirebaseFirestore.getInstance(),
            FirebaseAuth.getInstance()
        )
    }

    val menuViewModel: MenuViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MenuViewModel(menuRepository, imageRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    )

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // LINNÉA !! Detta är en fix för att lösa att vi laddar hem rätt profil samt återställer states
    //  dubbelkolla gärna mvh Patrik
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            profileViewModel.loadProfile()
            menuViewModel.observeMenu()
        } else {
            profileViewModel.resetState()
            locationViewModel.resetLocationForm()
            menuViewModel.resetState()
        }
    }

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
                ProfileScreen(navController = navController,
                    authViewModel= authViewModel,
                    profileViewModel = profileViewModel)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate("login"){
                        popUpTo(0)
                    }
                }
            }

        }

        composable ("edit_profile") {
            EditProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                locationViewModel = locationViewModel,
                menuViewModel = menuViewModel
            )
        }

        composable ("edit_menu/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")

            EditMenuScreen(
                navController = navController,
                viewModel = menuViewModel,
                itemId = if (itemId == "new") null else itemId
            )
        }
    }
}