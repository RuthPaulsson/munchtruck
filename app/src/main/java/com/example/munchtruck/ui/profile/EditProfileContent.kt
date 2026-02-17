package com.example.munchtruck.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
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
            // ===== TODO: Image section =====
            // ===== TODO: Input fields =====
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileContentPreview() {
    AppPreviewWrapper {
        EditProfileContent(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}