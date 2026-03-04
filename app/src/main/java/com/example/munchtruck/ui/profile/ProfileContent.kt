package com.example.munchtruck.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.ui.theme.Dimens.SpaceXS
import com.example.munchtruck.R
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.ui.components.CenteredLoading
import com.example.munchtruck.ui.components.FoodTypeSection
import com.example.munchtruck.ui.components.ItemCard
import com.example.munchtruck.ui.components.OpeningHoursSection
import com.example.munchtruck.ui.theme.Dimens
import com.example.munchtruck.ui.theme.Dimens.HeroHeight
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageIconSize
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageRadius
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageSize
import com.example.munchtruck.ui.theme.Dimens.SpaceM

// ====== Profile Content ===============================
@Composable
fun ProfileContent (
    isOwner: Boolean,
    truckName: String,
    foodType: String?,
    description: String,
    rating: String?,
    location: String?,
    openingHours: OpeningHours? = null,
    imageUrl: String?,
    menuItems: List<MenuItem>,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit
) {
    // ====== Root Container ===============================
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // ====== Hero Section ===============================

            ProfileHeroSection(
                isOwner = isOwner,
                truckName = truckName,
                imageUrl = imageUrl,
                onEditClick = onEditClick,
                onLogoutClick = onLogoutClick
            )

            errorMessage?.let {
                com.example.munchtruck.ui.components.InlineError(
                    message = it,
                    modifier = Modifier.padding(SpaceM)
                )
            }

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Info Section ===============================

            if (
                !rating.isNullOrBlank() ||
                !location.isNullOrBlank() ||
                openingHours != null
            ) {
                ProfileInfoRow(
                    rating = rating,
                    location = location,
                    foodType = foodType,
                    openingHours = openingHours
                )

                Spacer(modifier = Modifier.height(SpaceL))
            }

            // ====== About Section ===============================

            if(!description.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.profile_about),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = SpaceM)
                )
            }

            Spacer(modifier = Modifier.height(SpaceS))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = SpaceM)
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Menu Section ===============================

            if (menuItems.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.profile_menu),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = SpaceM)
                )

                Spacer(modifier = Modifier.height(SpaceS))

                Column(modifier = Modifier.padding(horizontal = SpaceM)) {
                    menuItems.forEach { item ->
                        // NY KOD: Här använder vi nu den gemensamma ItemCard istället för den gamla interna funktionen
                        ItemCard(
                            title = item.name,
                            description = item.description,
                            imageUrl = item.imageUrl,
                            priceOrInfo = stringResource(
                                R.string.menu_price_format,
                                item.price / 100,
                                stringResource(R.string.currency_sek)
                            )
                        )
                        Spacer(modifier = Modifier.height(SpaceS))
                    }
                }
            }
        }

        if (isLoading) {
            CenteredLoading()
        }
    }
}


// ====== Hero Section ===============================

@Composable
fun ProfileHeroSection(
    isOwner: Boolean,
    truckName: String,
    imageUrl: String?,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit
) {


    var expanded by remember { mutableStateOf(false) }

    // ====== Hero Container ===============================

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HeroHeight),
        contentAlignment = Alignment.Center

    ) {

        // ====== Image / Placeholder ===============================

        if (!imageUrl.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.height(MenuItemImageIconSize)
                )
            }
        }

        // ====== Owner Menu ===============================

        if (isOwner) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(SpaceM)
            ){
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.profile_options)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.profile_edit)) },
                        onClick = {
                            expanded = false
                            onEditClick()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.profile_logout)) },
                        onClick = {
                            expanded = false
                            onLogoutClick()
                        }
                    )
                }
            }
        }

        // ====== Truck Name ===============================

        Text(
            text = truckName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(SpaceM)
        )
    }
}

// ====== Info Row ===============================

@Composable
fun ProfileInfoRow(
    rating: String?,
    location: String?,
    foodType: String?,
    openingHours: OpeningHours?
) {
    // ====== State ===============================

    var isExpanded by remember { mutableStateOf(false) }

    // ====== Row Container ===============================

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceM)
    ) {
        // ====== Row 1: Rating, Location & Opening Hours ===============================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rating
            if (!rating.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(SpaceXS))
                Text(text = rating, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(SpaceS))
                Text(text = "•", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(SpaceS))
            }

            // Location
            if (!location.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(SpaceXS))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Opening Hours Toggle
            if (openingHours != null) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(SpaceS))
                        .clickable { isExpanded = !isExpanded }
                        .padding(start = SpaceS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(SpaceXS))
                    Text(
                        text = stringResource(R.string.opening_hours_title),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ====== Row 2: Food Type (Added more vertical space here) ===============================

        if (!foodType.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(SpaceS)) // Skapar avståndet ner till mattypen

            Text(
                text = foodType,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = if (!rating.isNullOrBlank()) 22.dp else 0.dp)
            )
        }

        // ====== Opening Hours Section (Expanded) ===============================

        if (openingHours != null) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = SpaceM) // Extra space for the dropdown
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            RoundedCornerShape(MenuItemImageRadius)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(MenuItemImageRadius)
                        )
                        .padding(SpaceM)
                ) {
                    OpeningHoursSection(
                        openingHours = openingHours,
                        isReadOnly = true
                    )
                }
            }
        }
    }
}


// ====== Preview ===============================

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    AppPreviewWrapper() {
        ProfileContent(
            isOwner = true,
            foodType = "",
            truckName = "Crazy Burgers",
            description = "Best smash burgers in town.",
            rating = "4.7",
            location = "Stockholm",
            openingHours = OpeningHours(),
            imageUrl = null,
            menuItems = listOf(
                MenuItem("1", "Classic Burger", 9500, "Best burger"),
                MenuItem("2", "Classic Fries", 7500, "Crispy fries")
            ),
            onLogoutClick = {},
            onEditClick = {}
        )
    }
}