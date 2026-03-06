package com.example.munchtruck.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.munchtruck.ui.theme.AppColors.DarkOverlay
import com.example.munchtruck.ui.theme.AppColors.InputBorder
import com.example.munchtruck.ui.theme.AppColors.LinkColor
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrangeLowAlpha
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.White

// ====== Color Schemes (Theme Layer) ===============================

private val LightColorScheme = lightColorScheme(
    primary = PrimaryOrange,
    onPrimary = White,
    secondary = PrimaryOrange,
    onSecondary = White,
    tertiary = LinkColor,
    onTertiary = PrimaryText,
    background = PrimaryBackground,
    onBackground = PrimaryText,
    surface = White,
    onSurface = PrimaryText,
    surfaceVariant = PrimaryOrangeLowAlpha,
    onSurfaceVariant = PrimaryText.copy(alpha = 0.6f),
    outline = InputBorder,
    scrim = DarkOverlay
)

private val DarkColorScheme = LightColorScheme

// ====== Theme Definition (Theme Layer) ===============================

@Composable
fun MunchtruckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}