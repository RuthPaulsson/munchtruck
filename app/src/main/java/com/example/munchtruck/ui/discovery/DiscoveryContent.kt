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
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.CenteredMessage
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.CardRadiusLarge
import com.example.munchtruck.ui.theme.Dimens.ChipHorizontalPadding
import com.example.munchtruck.ui.theme.Dimens.ChipRadius
import com.example.munchtruck.ui.theme.Dimens.ChipSpacing
import com.example.munchtruck.ui.theme.Dimens.ChipVerticalPadding
import com.example.munchtruck.ui.theme.Dimens.DiscoveryHeroHeight
import com.example.munchtruck.ui.theme.Dimens.HeroLogoTopPadding
import com.example.munchtruck.ui.theme.Dimens.HeroLogoWidth
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SearchFieldRadius
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.ui.theme.Dimens.SpaceSM
import com.example.munchtruck.ui.theme.Dimens.SpaceXL
import com.example.munchtruck.ui.theme.Dimens.SpaceXXL
import com.example.munchtruck.ui.theme.Dimens.SpaceXXXL
import com.example.munchtruck.ui.theme.Dimens.TruckImageRadius
import com.example.munchtruck.ui.theme.Dimens.TruckImageSize
import com.example.munchtruck.util.DistanceUtils.formatDistance
import com.example.munchtruck.viewmodels.DiscoveryUiState




@Composable
fun DiscoveryContent(
    uiState: DiscoveryUiState,
    errorMessage: String?,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onTruckClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    HeroSection(
                        searchQuery = searchQuery,
                        onSearchChange = onSearchChange
                    )
                }

                // Felhantering med din Retry-komponent
                if (errorMessage != null) {
                    item {
                        CenteredMessageWithRetry(
                            message = errorMessage,
                            onRetry = onRefresh
                        )
                    }
                }

                // Tom lista
                if (uiState.isListEmpty && errorMessage == null) {
                    item {
                        CenteredMessage(
                            message = stringResource(R.string.discovery_empty_nearby)
                        )
                    }
                }

                items(uiState.trucks) { truck ->
                    TruckItem(
                        truck = truck,
                        userLocation = uiState.userLocation,
                        onClick = { onTruckClick(truck.id) }
                    )
                }
            }
        }

        // Använd din CenteredLoading istället för manuell spinner
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
    searchQuery: String,
    onSearchChange: (String) -> Unit
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

            InputField(
                value = searchQuery,
                onChange = onSearchChange,
                placeholder = stringResource(R.string.discovery_search_placeholder),
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
                shape = RoundedCornerShape(SearchFieldRadius),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )

            )

            Spacer(modifier = Modifier.height(SpaceSM))

            Row(
                horizontalArrangement = Arrangement.spacedBy(SpaceSM)
            ) {
                CategoryChip(stringResource(R.string.discovery_category_burger), "🍔")
                CategoryChip(stringResource(R.string.discovery_category_tacos), "🌮")
                CategoryChip(stringResource(R.string.discovery_category_pizza), "🍕")
            }
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
                    color = Color.White
                )

                Text(
                    text = stringResource(R.string.discovery_hero_title_line2),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(SpaceS))

                Text(
                    text = stringResource(R.string.discovery_hero_subtitle),
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
        shape = RoundedCornerShape(ChipRadius),
        modifier = Modifier.clickable { }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = ChipHorizontalPadding, vertical = ChipVerticalPadding)
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(ChipSpacing))

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
            .padding(SpaceM, vertical = SpaceS)
            .clickable{ onClick() },
        shape = RoundedCornerShape(CardRadiusLarge)

    ) {
        Row(
            modifier = Modifier
                .padding(SpaceM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = truck.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(TruckImageSize)
                    .clip(
                        RoundedCornerShape(
                            topStart = TruckImageRadius,
                            bottomStart = TruckImageRadius,
                            topEnd = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
            )
            Spacer(modifier = Modifier.width(SpaceM))

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
            Spacer(modifier = Modifier.width(SpaceM))
            AsyncImage(
                model = truck.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(TruckImageSize)
                    .clip(
                        RoundedCornerShape(
                            topStart = TruckImageRadius,
                            bottomStart = TruckImageRadius,
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
                error = null,
                isListEmpty = false
            ),
            errorMessage = "",
            searchQuery= searchQuery.value,
            onSearchChange = { searchQuery.value = it },
            onRefresh = {},
            onTruckClick = {}
        )
    }
}