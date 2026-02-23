package com.example.munchtruck.ui.profile

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.R
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.LocationMapHeight
import com.example.munchtruck.ui.theme.Dimens.ProfileImageButtonRadius
import com.example.munchtruck.ui.theme.Dimens.ProfileImageHeight
import com.example.munchtruck.ui.theme.Dimens.ProfileImageRadius
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.viewmodels.LocationUiState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    name: String,
    description: String,
    foodType: String,
    locationState: LocationUiState,
    menuItems: List<MenuItem>,
    onEditMenuClick: (String) -> Unit,
    onDeleteMenuClick: (String) -> Unit,
    onMapPicked: (Double, Double) -> Unit,
    onUseCurrentLocation: () -> Unit,
    onSaveLocation: () -> Unit,
    selectedImageUri: Uri?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFoodTypeChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onImageClick: () -> Unit,
    onMenuClick: () -> Unit,
    snackbarHost: @Composable () -> Unit
) {
    var isLocationExpanded by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val hasLocation =
        locationState.selectedLat != null &&
                locationState.selectedLng != null



    Scaffold(
        snackbarHost = { snackbarHost() },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_profile_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.common_back)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onSaveClick) {
                        Text(stringResource(R.string.common_save))
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .animateContentSize()
                .padding(innerPadding)
                .padding(ScreenPadding)
        ) {

            // ===== Profile Image =====

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ProfileImageHeight)
                    .clip(RoundedCornerShape(ProfileImageRadius))
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Button(
                    onClick = onImageClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = SpaceM),
                    shape = RoundedCornerShape(ProfileImageButtonRadius)
                ) {
                    Text(stringResource(R.string.profile_load_image))
                }
            }

            Spacer(modifier = Modifier.height(SpaceL))

            // ===== Inputs =====

            InputField(
                value = name,
                onChange = onNameChange,
                placeholder = stringResource(R.string.profile_name_hint)
            )

            Spacer(modifier = Modifier.height(SpaceL))

            InputField(
                value = description,
                onChange = onDescriptionChange,
                placeholder = stringResource(R.string.profile_description_hint),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(SpaceL))

            InputField(
                value = foodType,
                onChange = onFoodTypeChange,
                placeholder = stringResource(R.string.profile_food_type_hint)
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ===== Location Row =====

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isLocationExpanded = !isLocationExpanded }
                    .padding(vertical = SpaceM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(SpaceS))

                Text(
                    text = if (hasLocation)
                        locationState.address
                    else
                        stringResource(R.string.profile_add_location),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (hasLocation)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            if (isLocationExpanded) {

                Spacer(modifier = Modifier.height(SpaceS))

                LocationMap(
                    lat = locationState.selectedLat,
                    lng = locationState.selectedLng,
                    onMapClick = onMapPicked
                )

                Spacer(modifier = Modifier.height(SpaceS))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpaceM)
                ) {

                    TextButton(onClick = onUseCurrentLocation) {
                        Text(stringResource(R.string.profile_use_current_location))
                    }

                    TextButton(onClick = onSaveLocation) {
                        Text(stringResource(R.string.profile_save_location))
                    }
                }

                Spacer(modifier = Modifier.height(SpaceL))
            }

            // ===== Menu Row =====

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable{ isMenuExpanded = !isMenuExpanded }
                    .padding(vertical = SpaceM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(SpaceS))

                Text(
                    text = stringResource(R.string.profile_menu),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            if (isMenuExpanded) {
                Spacer(modifier = Modifier.height(SpaceS))

                if (menuItems.isEmpty()) {
                    Text(
                        text = stringResource(R.string.menu_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    menuItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEditMenuClick(item.id) }
                                .padding(vertical = SpaceS),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.RestaurantMenu,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(SpaceS))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name)

                                Text(
                                    text = "\${item.price / 100} kr",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { onDeleteMenuClick(item.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(SpaceL))
            }

            // ===== Manage Menu =====

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMenuClick() }
                    .padding(vertical = SpaceM),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(SpaceS))

                Text(
                    text = stringResource(R.string.profile_manage_menu),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LocationMap(
    lat: Double?,
    lng: Double?,
    onMapClick: (Double, Double) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocationMapHeight),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onMapClick(latLng.latitude, latLng.longitude)
        }
    ) {
        if (lat != null && lng != null) {
            Marker(
                state = MarkerState(
                    position = LatLng(lat, lng)
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditProfileContentPreview() {
    AppPreviewWrapper {
        EditProfileContent(
            name = "",
            description = "",
            foodType = "",
            locationState = LocationUiState(),
            selectedImageUri = null,
            onMapPicked = { _, _ -> },
            onUseCurrentLocation = {},
            onSaveLocation = {},
            onBackClick = {},
            onSaveClick = {},
            onImageClick = {},
            onNameChange = {},
            onDescriptionChange = {},
            onFoodTypeChange = {},
            onMenuClick = {},
            menuItems = listOf(
                MenuItem("1", "Classic Burger", 9500, "Best burger"),
                MenuItem("2", "Fries", 7500, "Crispy fries")
            ),
            onEditMenuClick = {},
            onDeleteMenuClick = {},
            snackbarHost = {}
        )
    }
}