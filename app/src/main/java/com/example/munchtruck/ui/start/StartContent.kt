package com.example.munchtruck.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.DarkAuthBackground
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LogoHeightSmall
import com.example.munchtruck.ui.theme.Dimens.LogoWidthSmall
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.StartTopSpacing
import com.example.munchtruck.ui.theme.Dimens.TitleLarge

// ====== Start Content (UI Layer) ===============================

@Composable
fun StartContent(
    onOwnerClick: () -> Unit,
    onLoverClick: () -> Unit
) {
    DarkAuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ====== Header / Welcome Section ===============================

            Spacer(modifier = Modifier.height(StartTopSpacing))

            Text(
                text = stringResource(R.string.start_welcome),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = TitleLarge
                ),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )

            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.logo_munchtruck),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(LogoWidthSmall)
                    .height(LogoHeightSmall)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            // ====== Action Buttons ===============================

            Button(
                onClick = onOwnerClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.start_owner_title),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = stringResource(R.string.start_owner_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpaceM))

            Button(
                onClick = onLoverClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.start_lover_title),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = stringResource(R.string.start_lover_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// ====== Preview ===============================

@Preview
@Composable
fun StartContentPreview() {
    AppPreviewWrapper {
        StartContent(
            onOwnerClick = {},
            onLoverClick = {}
        )
    }
}