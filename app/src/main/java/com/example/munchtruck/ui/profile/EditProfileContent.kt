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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.R
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
import com.example.munchtruck.ui.theme.Dimens.SpaceXS
import com.example.munchtruck.ui.theme.Dimens.TopBarLogoHeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    name: String,
    description: String,
    foodType: String,
    location: String,
    selectedImageUri: Uri?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFoodTypeChange: (String) -> Unit,
    onLocationSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onImageClick: () -> Unit,
    onMenuClick: () -> Unit,
    snackbarHost: @Composable () -> Unit
) {
    var isLocationExpanded by remember { mutableStateOf(false) }
    var tempLocation by remember(location) { mutableStateOf(location) }
    val hasLocation = location.isNotBlank()
    val currentLocationText =
        stringResource(R.string.profile_use_current_location)

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
                    text = location.ifBlank {
                        stringResource(R.string.profile_add_location)
                    },
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocationMapHeight)
                        .clip(RoundedCornerShape(ProfileImageRadius))
                ) {
                    Text(
                        text = stringResource(R.string.profile_map_placeholder),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(SpaceS))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpaceM)
                ) {

                    TextButton(
                        onClick = {
                            tempLocation = currentLocationText
                        }
                    ) {
                        Text(stringResource(R.string.profile_use_current_location))
                    }

                    TextButton(
                        onClick = {
                            onLocationSelected(tempLocation)
                            isLocationExpanded = false
                        }
                    ) {
                        Text(stringResource(R.string.profile_save_location))
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

@Preview(showBackground = true)
@Composable
fun EditProfileContentPreview() {
    AppPreviewWrapper {
        EditProfileContent(
            name = "",
            description = "",
            foodType = "",
            selectedImageUri = null,
            onBackClick = {},
            onSaveClick = {},
            onImageClick = {},
            onNameChange = {},
            onDescriptionChange = {},
            onFoodTypeChange = {},
            onMenuClick = {},
            snackbarHost = {},
            location = "",
            onLocationSelected = {}
        )
    }
}