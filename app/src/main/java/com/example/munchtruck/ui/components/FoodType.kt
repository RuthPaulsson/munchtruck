package com.example.munchtruck.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.Dimens.ChipHorizontalPadding
import com.example.munchtruck.ui.theme.Dimens.ChipRadius
import com.example.munchtruck.ui.theme.Dimens.ChipSpacing
import com.example.munchtruck.ui.theme.Dimens.ChipVerticalPadding
import com.example.munchtruck.ui.theme.Dimens.IconSizeSmall
import com.example.munchtruck.ui.theme.Dimens.SpaceXS

// ====== Food Type Section (UI Layer) ===============================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodTypeSection(
    foodType: String,
    onFoodTypeChange: ((String) -> Unit)? = null,
    isReadOnly: Boolean = false
) {
    val foodOptions = listOf(
        R.string.food_type_burger,
        R.string.food_type_tacos,
        R.string.food_type_pizza
    )

    if (isReadOnly) {
        if (foodType.isNotBlank()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = null,
                    modifier = Modifier.size(IconSizeSmall),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(SpaceXS))
                Text(
                    text = foodType,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } else {
        var expanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.profile_food_type_hint),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = SpaceXS)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = foodType,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(stringResource(R.string.profile_food_type_placeholder)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    foodOptions.forEach { resId ->
                        val label = stringResource(resId)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = label,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onFoodTypeChange?.invoke(label)
                                expanded = false
                            },
                        )
                    }
                }
            }
        }
    }
}

// ====== Food Type Filter Bar (UI Layer) ===============================

@Composable
fun FoodTypeFilterBar(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val foodOptions = listOf(
        R.string.food_type_burger to R.drawable.icon_burger,
        R.string.food_type_tacos to R.drawable.icon_taco,
        R.string.food_type_pizza to R.drawable.icon_pizza
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        foodOptions.forEach { (resId, iconRes) ->
            val label = stringResource(resId)
            val isSelected = selectedCategory == label

            if (foodOptions.indexOf(resId to iconRes) != 0) {
                Spacer(modifier = Modifier.width(ChipSpacing))
            }

            Card(
                shape = RoundedCornerShape(ChipRadius),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                modifier = Modifier.clickable {
                    if (isSelected) onCategoryClick("") else onCategoryClick(label)
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = ChipHorizontalPadding,
                        vertical = ChipVerticalPadding
                    )
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(IconSizeSmall)
                    )

                    Spacer(modifier = Modifier.width(ChipSpacing))

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            }
        }
    }
}

// ====== Food Type Helpers (UI Layer) ===============================

@Composable
fun getFoodTypeImage(foodType: String?): Int? {
    return when (foodType) {
        stringResource(R.string.food_type_burger) -> R.drawable.img_burger
        stringResource(R.string.food_type_tacos) -> R.drawable.img_tacos
        stringResource(R.string.food_type_pizza) -> R.drawable.img_pizza
        else -> null
    }
}