package com.example.munchtruck.ui.discovery

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.data.model.FoodTruck
import com.example.munchtruck.data.model.TruckLocation
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.CenteredMessage
import com.example.munchtruck.ui.components.DiscoveryBottom
import com.example.munchtruck.ui.components.FoodTypeFilterBar
import com.example.munchtruck.ui.components.ItemCard
import com.example.munchtruck.ui.components.getFoodTypeImage
import com.example.munchtruck.ui.components.isOpenNow
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.BottomNavHeight
import com.example.munchtruck.ui.theme.Dimens.DiscoveryHeroHeight
import com.example.munchtruck.ui.theme.Dimens.HeroLogoTopPadding
import com.example.munchtruck.ui.theme.Dimens.HeroLogoWidth
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.ui.theme.Dimens.SpaceXXL
import com.example.munchtruck.ui.theme.Dimens.SpaceXXXL
import com.example.munchtruck.util.DistanceUtils.formatDistance
import com.example.munchtruck.viewmodels.DiscoveryUiState

// ====== Discovery Content (UI Layer) ===============================

@Composable
fun DiscoveryContent(
    uiState: DiscoveryUiState,
    errorMessage: String?,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onTruckClick: (String) -> Unit,
    onMapClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val refreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            state = refreshState,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // ====== Hero Section ===============================

                item {
                    HeroSection(
                        selectedCategory = selectedCategory,
                        onCategoryChange = onCategoryChange
                    )
                }

                // ====== Message States ===============================

                if (errorMessage != null) {
                    item {
                        CenteredMessage(message = errorMessage)
                    }
                }

                if (uiState.isListEmpty && errorMessage == null) {
                    item {
                        CenteredMessage(
                            message = stringResource(R.string.discovery_message_empty_nearby)
                        )
                    }
                }

                // ====== Truck List ===============================

                items(uiState.trucks) { truck ->
                    val isOpen = truck.openingHours?.isOpenNow() ?: false
                    val statusText = if (isOpen) {
                        stringResource(R.string.status_label_open)
                    } else {
                        stringResource(R.string.status_label_closed)
                    }

                    val calculatedDistance = uiState.userLocation?.let { userLoc ->
                        truck.location?.let { loc ->
                            val truckLoc = Location("").apply {
                                latitude = loc.latitude
                                longitude = loc.longitude
                            }
                            formatDistance(userLoc.distanceTo(truckLoc))
                        }
                    }

                    val foodImage = getFoodTypeImage(truck.foodType)

                    ItemCard(
                        title = truck.name,
                        description = statusText,
                        distance = calculatedDistance,
                        imageUrl = truck.imageUrl,
                        priceOrInfo = "", 
                        trailingImageRes = foodImage,
                        modifier = Modifier
                            .padding(horizontal = SpaceM, vertical = SpaceS)
                            .clickable { onTruckClick(truck.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(BottomNavHeight))
                }
            }
        }

        // ====== Navigation ===============================

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            DiscoveryBottom(
                currentRoute = "home",
                onMapClick = onMapClick,
                onHomeClick = onHomeClick
            )
        }

        if (uiState.isLoading && uiState.trucks.isEmpty()) {
            CenteredLoading()
        }
    }
}

// ====== Hero Section (UI Layer) ===============================

@Composable
fun HeroSection(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DiscoveryHeroHeight)
    ) {
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
                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.common_content_desc_logo),
                modifier = Modifier
                    .padding(top = HeroLogoTopPadding)
                    .width(HeroLogoWidth)
            )

            Spacer(modifier = Modifier.height(SpaceXXL))

            Text(
                text = stringResource(R.string.discovery_placeholder_search),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(SpaceM))

            FoodTypeFilterBar(
                selectedCategory = selectedCategory,
                onCategoryClick = onCategoryChange
            )

            Spacer(modifier = Modifier.height(SpaceXXXL))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = SpaceXXL),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.discovery_hero_title_1),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = stringResource(R.string.discovery_hero_title_2),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(SpaceS))

                Text(
                    text = stringResource(R.string.discovery_hero_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// ====== Preview ===============================

@Preview(showBackground = true)
@Composable
fun DiscoveryContentPreview() {
    AppPreviewWrapper {
        val mockTrucks = listOf(
            FoodTruck(
                id = "1",
                name = "Burger Truck",
                description = "Best burgers",
                foodType = "Burger",
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

        DiscoveryContent(
            uiState = DiscoveryUiState(
                trucks = mockTrucks,
                isLoading = false,
                error = null,
                isListEmpty = false
            ),
            errorMessage = null,
            selectedCategory = "",
            onCategoryChange = {},
            onRefresh = {},
            onTruckClick = {},
            onMapClick = {},
            onHomeClick = {}
        )
    }
}
