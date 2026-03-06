package com.example.munchtruck.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.Dimens.LogoHeightSmall
import com.example.munchtruck.ui.theme.Dimens.LogoWidthSmall
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceS

// ====== Background with bottom illustration ===============================

@Composable
fun AuthBackground(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.basic_car),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align (Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )
        content()
    }
}

// ====== Header with logo and subtitle =========================

@Composable
fun AuthHeader(
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(SpaceL))

        Image(
            painter = painterResource(R.drawable.munchtruck_text),
            contentDescription = stringResource(R.string.logo_munchtruck),
            modifier = Modifier
                .fillMaxWidth(LogoWidthSmall)
                .height(LogoHeightSmall),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(SpaceS))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(SpaceL))
    }
}