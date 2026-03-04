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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.Dimens
import com.example.munchtruck.ui.theme.Dimens.CardElevation
import com.example.munchtruck.ui.theme.Dimens.MenuItemCardHeight
import com.example.munchtruck.ui.theme.Dimens.MenuItemImageRadius
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.ui.theme.Dimens.SpaceXS

// ====== Item Card ===============================

@Composable
fun ItemCard(
    title: String,
    description: String,
    imageUrl: String?,
    priceOrInfo: String,
    trailingImageRes: Int? = null, // <--- DETTA ÄR KOMPLETTERINGEN
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(MenuItemCardHeight),
        shape = RoundedCornerShape(MenuItemImageRadius),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = CardElevation)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ====== Image Section (Vänster) ======
            Box(
                modifier = Modifier
                    .width(Dimens.MenuItemImageWidth)
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

            // ====== Content Section (Mitten) ======
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
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(SpaceXS))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(SpaceS))

                // Om vi INTE har en höger-bild, visar vi texten här (Pris i Profile)
                if (trailingImageRes == null) {
                    Text(
                        text = priceOrInfo,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ====== Trailing Image Section (Höger - ENDAST DISCOVERY) ======
            if (trailingImageRes != null) {
                Image(
                    painter = painterResource(id = trailingImageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(Dimens.MenuItemImageWidth)
                        .fillMaxHeight()
                )
            }
        }
    }
}