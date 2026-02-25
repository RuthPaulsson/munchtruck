package com.example.munchtruck.ui.discovery


import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.munchtruck.R
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.util.DistanceUtils.formatDistance
import com.example.munchtruck.viewmodels.DiscoveryUiState




@Composable
fun DiscoveryContent(
    uiState: DiscoveryUiState,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onTruckClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    HeroSection(
                        searchQuery = searchQuery,
                        onSearchChange = onSearchChange
                    )
                }

                // Error state
                if (uiState.errorMessage != null) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(uiState.errorMessage)
                        }
                    }
                }

                // Empty state
                if (uiState.isListEmpty) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No trucks near you")
                        }
                    }
                }

                // List
                items(uiState.trucks) { truck ->
                    TruckItem(
                        truck = truck,
                        userLocation = uiState.userLocation,
                        onClick = { onTruckClick(truck.id) }
                    )
                }
            }
        }

        // Center spinner ONLY on first load
        if (uiState.isLoading && uiState.trucks.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun HeroSection(
    searchQuery: String,
    onSearchChange: (String) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
    ){
        Image(
            painter = painterResource(R.drawable.discovery_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.35f)
                        )
                    )
                )
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.logo_munchtruck),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(200.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            InputField(
                value = searchQuery,
                onChange = onSearchChange,
                placeholder = "What are you craving?",
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )

            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CategoryChip("Burger", "🍔")
                CategoryChip("Tacos", "🌮")
                CategoryChip("Pizza", "🍕")
            }
            Spacer(modifier = Modifier.height(45.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Discover burgers",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Text(
                    text = "near you",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tasty burgers delivered quickly",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }

        }

    }
}

@Composable
fun CategoryChip(text: String, emoji: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
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
            .padding(16.dp, vertical = 8.dp)
            .clickable{ onClick() },
        shape = RoundedCornerShape(20.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = truck.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = truck.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                truck.location?.let { location ->
                    userLocation?.let { userLoc ->
                        val truckLoc = Location("").apply {
                            latitude = location.latitude
                            longitude = location.longitude
                        }
                        val distance = userLoc.distanceTo(truckLoc)
                        Text(
                            text = formatDistance(distance),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = truck.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
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
                    updatedAt = 0L
                )
            )
        )
        var searchQuery = remember { mutableStateOf("") }
        DiscoveryContent(
            uiState = DiscoveryUiState(
                trucks = mockTrucks,
                isLoading = false,
                errorMessage = null,
                isListEmpty = false
            ),
            searchQuery= searchQuery.value,
            onSearchChange = { searchQuery.value = it },
            onRefresh = {},
            onTruckClick = {}
        )
    }
}