package com.example.munchtruck.ui.profile

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    name: String,
    description: String,
    foodType: String,
    selectedImageUri: Uri?,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onFoodTypeChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onImageClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painterResource(R.drawable.munchtruck_text),
                        contentDescription = stringResource(R.string.logo_munchtruck),
                        modifier = Modifier.height(28.dp)
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
                .padding(innerPadding)
                .padding(ScreenPadding)

        ) {
            Spacer(modifier = Modifier.height(SpaceL))
            Text(
                text = stringResource(R.string.edit_profile_title),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(SpaceL))

        // ===== Profile Image Section =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                if (selectedImageUri != null){
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =  Modifier.fillMaxSize()
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
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(50)

                ) {
                    Text(stringResource(R.string.profile_load_image))
                }
            }
            Spacer(modifier = Modifier.height(SpaceL))

            // ===== Input fields =====

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
            onFoodTypeChange = {}
        )
    }
}