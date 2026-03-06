package com.example.munchtruck.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.AppColors.StatusClosed
import com.example.munchtruck.ui.theme.AppColors.StatusOpen
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.CardElevation
import com.example.munchtruck.ui.theme.Dimens.MenuItemCardHeight
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageIconSize
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageRadius
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageWidth
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceXS

// ====== Item Card (UI Layer) ===============================

@Composable
fun ItemCard(
    title: String,
    description: String,
    imageUrl: String?,
    priceOrInfo: String,
    distance: String? = null,
    trailingImageRes: Int? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(MenuItemCardHeight),
        shape = RoundedCornerShape(MenuItemImageRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = CardElevation)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(MenuItemImageWidth)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
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
                    Icon(
                        imageVector = Icons.Default.RestaurantMenu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(SpaceM),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(SpaceXS))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val openText = stringResource(R.string.status_open)
                        val statusColor = if (description.contains(openText, ignoreCase = true))
                            StatusOpen else StatusClosed

                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor
                        )

                        if (!distance.isNullOrBlank()) {
                            Text(
                                text = " • $distance",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }

                    if (priceOrInfo.isNotBlank()) {
                        Spacer(modifier = Modifier.height(SpaceXS))
                        Text(
                            text = priceOrInfo,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1
                        )
                    }
                }
            }

            if (trailingImageRes != null) {
                Image(
                    painter = painterResource(id = trailingImageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(MenuItemImageWidth)
                        .fillMaxHeight()
                )
            }
        }
    }
}

// ====== Image Placeholder (UI Layer) ===============================

@Composable
fun SharedImagePlaceholder(
    modifier: Modifier = Modifier,
    selectedImageUri: android.net.Uri? = null,
    existingImageUrl: String? = null,
    onImageClick: (() -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        val painter = when {
            selectedImageUri != null -> rememberAsyncImagePainter(selectedImageUri)
            !existingImageUrl.isNullOrBlank() -> rememberAsyncImagePainter(existingImageUrl)
            else -> null
        }

        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.RestaurantMenu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.height(MenuItemImageIconSize)
            )
        }

        onImageClick?.let { onClick ->
            Button(
                onClick = onClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = SpaceM),
                shape = RoundedCornerShape(ButtonRadius)
            ) {
                Text(stringResource(R.string.menu_select_image))
            }
        }
    }
}