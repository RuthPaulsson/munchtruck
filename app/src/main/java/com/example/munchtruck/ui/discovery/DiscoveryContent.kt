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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.CenteredMessage
import com.example.munchtruck.ui.components.DiscoveryBottom
import com.example.munchtruck.ui.components.FoodTypeFilterBar
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.components.ItemCard
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.BottomNavHeight
import com.example.munchtruck.ui.theme.Dimens.ChipHorizontalPadding
import com.example.munchtruck.ui.theme.Dimens.ChipRadius
import com.example.munchtruck.ui.theme.Dimens.ChipSpacing
import com.example.munchtruck.ui.theme.Dimens.ChipVerticalPadding
import com.example.munchtruck.ui.theme.Dimens.DiscoveryCardRadius
import com.example.munchtruck.ui.theme.Dimens.DiscoveryHeroHeight
import com.example.munchtruck.ui.theme.Dimens.HeroLogoTopPadding
import com.example.munchtruck.ui.theme.Dimens.HeroLogoWidth
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SearchFieldRadius
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.ui.theme.Dimens.SpaceSM
import com.example.munchtruck.ui.theme.Dimens.SpaceXXL
import com.example.munchtruck.ui.theme.Dimens.SpaceXXXL
import com.example.munchtruck.util.DistanceUtils.formatDistance
import com.example.munchtruck.viewmodels.DiscoveryUiState


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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    HeroSection(
                        selectedCategory = selectedCategory,
                        onCategoryChange = onCategoryChange
                    )
                }

                if (errorMessage != null) {
                    item {
                        CenteredMessageWithRetry(
                            message = errorMessage,
                            onRetry = onRefresh
                        )
                    }
                }

                if (uiState.isListEmpty && errorMessage == null) {
                    item {
                        CenteredMessage(
                            message = stringResource(R.string.discovery_empty_nearby)
                        )
                    }
                }

                items(uiState.trucks) { truck ->

                    val statusText = if (truck.isOpen) {
                        stringResource(R.string.status_open)
                    } else {
                        stringResource(R.string.status_closed)
                    }

                    val distanceText = uiState.userLocation?.let { userLoc ->
                        truck.location?.let { loc ->
                            val truckLoc = Location("").apply {
                                latitude = loc.latitude
                                longitude = loc.longitude
                            }
                            " • ${formatDistance(userLoc.distanceTo(truckLoc))}"
                        }
                    } ?: ""

                    ItemCard(
                        title = truck.name,
                        description = "$statusText$distanceText",
                        imageUrl = truck.imageUrl,
                        priceOrInfo = truck.foodType ?: "",
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

@Composable
fun CenteredMessageWithRetry(message: String, onRetry: () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun HeroSection(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(DiscoveryHeroHeight)
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
                .padding(horizontal = ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.logo_munchtruck),
                modifier = Modifier
                    .padding(top = HeroLogoTopPadding)
                    .width(HeroLogoWidth)
            )

            Spacer(modifier = Modifier.height(SpaceXXL))

            Text(
                text = stringResource(R.string.discovery_search_placeholder),
                style = MaterialTheme.typography.titleMedium,
                color = White
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
                    text = stringResource(R.string.discovery_hero_title_line1),
                    style = MaterialTheme.typography.headlineSmall,
                    color = White
                )

                Text(
                    text = stringResource(R.string.discovery_hero_title_line2),
                    style = MaterialTheme.typography.headlineSmall,
                    color = White
                )

                Spacer(modifier = Modifier.height(SpaceS))

                Text(
                    text = stringResource(R.string.discovery_hero_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = White
                )
            }
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
            errorMessage = "",
            selectedCategory = "",
            onCategoryChange = {},
            onRefresh = {},
            onTruckClick = {},
            onMapClick = {},
            onHomeClick = {}
        )
    }
}