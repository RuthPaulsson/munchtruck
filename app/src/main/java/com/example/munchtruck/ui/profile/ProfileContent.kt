package com.example.munchtruck.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.data.model.MenuItem
import com.example.munchtruck.ui.theme.Dimens.SpaceXS

@Composable
fun ProfileContent (
    isOwner: Boolean,
    truckName: String,
    description: String,
    rating: String?,
    location: String?,
    openingHours: String?,
    imageUrl: String?,
    menuItems: List<MenuItem>,
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeroSection(
            isOwner = isOwner,
            truckName = truckName,
            imageUrl = imageUrl,
            onEditClick = onEditClick,
        )

        Spacer(modifier = Modifier.height(SpaceL))

        if (
            !rating.isNullOrBlank() ||
            !location.isNullOrBlank() ||
            !openingHours.isNullOrBlank()
        ) {
            ProfileInfoRow(
                rating = rating,
                location = location,
                openingHours = openingHours
            )

            Spacer(modifier = Modifier.height(SpaceL))
        }

        if(!description.isNullOrBlank()) {
            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(SpaceS))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(SpaceL))

        if (menuItems.isNotEmpty()) {

            Spacer(modifier = Modifier.height(SpaceL))

            Text(
                text = "Menu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(SpaceS))
           Column(modifier = Modifier.padding(horizontal = 16.dp)) {
               menuItems.forEach { item ->
                   ProfileMenuItemCard(item)
                   Spacer(modifier = Modifier.height(SpaceS))
               }
           }
        }

        if(isOwner) {
            Button(
                onClick = onLogoutClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Log out")
            }

            Spacer(modifier = Modifier.height(SpaceL))
        }

    }
}
@Composable
fun ProfileHeroSection(
    isOwner: Boolean,
    truckName: String,
    imageUrl: String?,
    onEditClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center

    ) {
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
                    .clip(RoundedCornerShape(0.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.height(64.dp)
                )
            }
        }
        if (isOwner) {
            TextButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Edit")
            }
        }

        Text(
            text = truckName,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
fun ProfileInfoRow(
    rating: String?,
    location: String?,
    openingHours: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!rating.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(SpaceXS))

            Text(
                text = rating,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (!rating.isNullOrBlank() && !location.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(SpaceS))

            Text(
                text = "•",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(SpaceS))
        }

        if (!location.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(SpaceXS))

            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (!openingHours.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(SpaceXS))

            Text(
                text = openingHours,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProfileMenuItemCard(item: MenuItem) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {

                if (!item.imageUrl.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.RestaurantMenu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )

                if (item.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(SpaceXS))

                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "${item.price / 100} kr",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    AppPreviewWrapper() {
        ProfileContent(
            isOwner = true,
            truckName = "Crazy Burgers",
            description = "Best smash burgers in town.",
            rating = "4.7",
            location = "Stockholm",
            openingHours = "Mon–Fri 11:00–20:00",
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