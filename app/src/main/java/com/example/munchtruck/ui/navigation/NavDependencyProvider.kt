package com.example.munchtruck.ui.navigation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.munchtruck.data.firebase.FirebaseAuthRepository
import com.example.munchtruck.data.firebase.FirebaseDiscoveryRepository
import com.example.munchtruck.data.firebase.FirebaseMenuRepository
import com.example.munchtruck.data.firebase.FirebaseProfileRepository
import com.example.munchtruck.data.firebase.StorageImageRepository
import com.example.munchtruck.data.location.FusedDeviceLocationProvider
import com.example.munchtruck.viewmodels.AuthViewModel
import com.example.munchtruck.viewmodels.DiscoveryViewModel
import com.example.munchtruck.viewmodels.LocationViewModel
import com.example.munchtruck.viewmodels.MenuViewModel
import com.example.munchtruck.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

// ====== Nav Dependency Provider (Infrastructure Layer) ===============================

class NavDependencyProvider(context: Context) {

    // ====== Firebase & Infrastructure ===============================

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val locationProvider = FusedDeviceLocationProvider(context)

    // ====== Repositories ===============================

    private val authRepository = FirebaseAuthRepository(auth, firestore)
    private val profileRepository = FirebaseProfileRepository(firestore, auth, storage)
    private val imageRepository = StorageImageRepository(storage, auth)
    private val menuRepository = FirebaseMenuRepository(firestore, auth)
    private val discoveryRepository = FirebaseDiscoveryRepository(firestore)

    // ====== ViewModel Factories ===============================

    val authFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthViewModel(authRepository) as T
    }

    val profileFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ProfileViewModel(
                profileRepository,
                imageRepository,
                authRepository
            ) as T
    }

    val locationFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            LocationViewModel(
                profileRepository,
                locationProvider
            ) as T
    }

    val menuFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MenuViewModel(
                menuRepository,
                imageRepository
            ) as T
    }

    val discoveryFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            DiscoveryViewModel(
                discoveryRepository,
                locationProvider,
                menuRepository,
                profileRepository
            ) as T
    }
}