package com.example.munchtruck.ui.menu

import android.net.Uri
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.components.SharedImagePlaceholder
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.MenuImageHeight
import com.example.munchtruck.ui.theme.Dimens.MenuImageRadius
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM

// ====== Edit Menu Content (UI Layer) ===============================

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
                                strokeWidth = LoaderStroke,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.common_save),
                                color = MaterialTheme.colorScheme.primary
                            )
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

            // ====== Image Section ===============================

            SharedImagePlaceholder(
                selectedImageUri = selectedImageUri,
                existingImageUrl = existingImageUrl,
                onImageClick = onImageClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MenuImageHeight)
                    .clip(RoundedCornerShape(MenuImageRadius))
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Input Form Section ===============================

            InputField(
                value = name,
                onChange = onNameChange,
                placeholder = stringResource(R.string.menu_name_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

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

// ====== Preview ===============================

@Preview(showBackground = true)
@Composable
fun EditMenuContentPreview() {
    AppPreviewWrapper {
        EditMenuContent(
            name = "Kebabtallrik",
            price = "120",
            description = "En klassiker med pommes och sås",
            priceError = null,
            selectedImageUri = null,
            isLoading = false,
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