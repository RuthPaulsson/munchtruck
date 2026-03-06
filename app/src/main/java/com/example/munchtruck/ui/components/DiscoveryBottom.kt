package com.example.munchtruck.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.Dimens.SpaceS

// ====== Discovery Bottom Navigation (UI Layer) ===============================

@Composable
fun DiscoveryBottom(
    currentRoute: String,
    onMapClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Surface(
        shadowElevation = SpaceS,
        color = MaterialTheme.colorScheme.surface
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
        ) {

            // ====== Home Item ===============================

            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = onHomeClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(R.string.discovery_nav_home),
                        tint = if (currentRoute == "home") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        }
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.discovery_nav_home),
                        color = if (currentRoute == "home") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )

            // ====== Map Item ===============================

            NavigationBarItem(
                selected = currentRoute == "map",
                onClick = onMapClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = stringResource(R.string.discovery_nav_map),
                        tint = if (currentRoute == "map") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        }
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.discovery_nav_map),
                        color = if (currentRoute == "map") {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}