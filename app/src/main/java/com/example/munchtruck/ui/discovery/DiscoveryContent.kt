package com.example.munchtruck.ui.discovery


import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.viewmodels.DiscoveryUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryContent(
    uiState: DiscoveryUiState,
    onRefresh: () -> Unit,
    onTruckClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Discovery") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ){
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.isListEmpty -> {
                    Text(
                        text = "No trucks near you",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    TruckList(
                        trucks = uiState.trucks,
                        userLocation = uiState.userLocation,
                        onTruckClick = onTruckClick
                    )
                }
            }
        }
    }
}

@Composable
fun TruckList(
    trucks: List<FoodTruck>,
    userLocation: Location?,
    onTruckClick: (String) -> Unit
) {
    LazyColumn {
        items(trucks) { truck ->
            TruckItem(
                truck = truck,
                userLocation = userLocation,
                onClick = { onTruckClick(truck.id) }
            )
        }
    }
}

@Composable
fun TruckItem(
    truck: FoodTruck,
    userLocation: Location?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable{ onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = truck.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiscoveryContentPreview() {
    AppPreviewWrapper {

        val mockTrucks = listOf(
            FoodTruck(
                id = "1",
                name = "Burger Truck",
                description = "Best burgers",
                foodType = "Burgers",
                imageUrl = "",
                isOpen = true,
                location = TruckLocation(
                    latitude = 59.33,
                    longitude = 18.06,
                    address = "Stockholm",
                    updatedAtMilis = 0L
                )
            )
        )

        DiscoveryContent(
            uiState = DiscoveryUiState(
                trucks = mockTrucks,
                isLoading = false,
                errorMessage = null,
                isListEmpty = false
            ),
            onRefresh = {},
            onTruckClick = {}
        )
    }
}