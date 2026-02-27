package com.example.munchtruck.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.Dimens.SpaceS

@Composable
fun DiscoveryBottom(
    currentRoute: String,
    onMapClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Surface(
        shadowElevation = SpaceS,
        color = White
    ) {
        NavigationBar(
            containerColor = White,
            tonalElevation = 0.dp,
        ) {
            // HOME ITEM
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = onHomeClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.discovery_nav_home),
                        tint = if (currentRoute == "home") PrimaryOrange else Color.LightGray
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.discovery_nav_home),
                        color = if (currentRoute == "home") PrimaryOrange else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

            NavigationBarItem(
                selected = currentRoute == "map",
                onClick = onMapClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.discovery_nav_map),
                        tint = if (currentRoute == "map") PrimaryOrange else Color.LightGray
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.discovery_nav_map),
                        color = if (currentRoute == "map") PrimaryOrange else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}