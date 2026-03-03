package com.example.munchtruck.ui.menu

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.MenuImageButtonBottomPadding
import com.example.munchtruck.ui.theme.Dimens.MenuImageHeight
import com.example.munchtruck.ui.theme.Dimens.MenuImageRadius
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMenuContent(
    name: String,
    price: String,
    description: String,
    selectedImageUri: Uri?,
    existingImageUrl: String? = null,
    isLoading: Boolean,
    priceError: String?,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onImageClick: () -> Unit,
    snackbarHost: @Composable () -> Unit
) {

    Scaffold(
        snackbarHost = { snackbarHost() },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(R.string.menu_title))
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
                    TextButton(
                        onClick = onSaveClick,
                        enabled = !isLoading,
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(LoaderSize),
                                strokeWidth = LoaderStroke
                            )
                        } else {
                            Text(stringResource(R.string.common_save))
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(ScreenPadding)
        ) {

            Spacer(modifier = Modifier.height(SpaceL))

            // ===== Image Section =====

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MenuImageHeight)
                    .clip(RoundedCornerShape(MenuImageRadius))
            ) {
                // Vi skapar en painter som kollar båda källorna
                val painter = when {
                    selectedImageUri != null -> rememberAsyncImagePainter(selectedImageUri)
                    !existingImageUrl.isNullOrBlank() -> rememberAsyncImagePainter(existingImageUrl)
                    else -> null
                }

                if (painter != null) {
                    // Om vi har antingen en ny bild eller en sparad bild, rita den!
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Om BÅDA är null, visa din placeholder-text
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.menu_no_image))
                    }
                }

                Button(
                    onClick = onImageClick,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = MenuImageButtonBottomPadding),
                    shape = RoundedCornerShape(ButtonRadius)
                ) {
                    Text(stringResource(R.string.menu_select_image))
                }
            }

            Spacer(modifier = Modifier.height(SpaceL))

            // ===== Dish Name =====

            InputField(
                value = name,
                onChange = onNameChange,
                placeholder = stringResource(R.string.menu_name_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            // ===== Price =====

            InputField(
                value = price,
                onChange = onPriceChange,
                placeholder = stringResource(R.string.menu_price_hint),
                errorMessage = priceError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Spacer(modifier = Modifier.height(SpaceM))

            // ===== Description =====

            InputField(
                value = description,
                onChange = onDescriptionChange,
                placeholder = stringResource(R.string.menu_description_hint),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(SpaceL))

        }
    }
}


@Preview(showBackground = true)
@Composable
fun EditMenuContentPreview() {
    AppPreviewWrapper {
        EditMenuContent(
            name = "",
            price = "",
            description = "",
            priceError= "",
            selectedImageUri = null,
            isLoading = true,
            onNameChange = {},
            onPriceChange = {},
            onDescriptionChange = {},
            onBackClick = {},
            onSaveClick = {},
            onImageClick = {},
            snackbarHost = {}
        )
    }
}