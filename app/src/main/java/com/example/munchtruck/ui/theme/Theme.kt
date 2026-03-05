package com.example.munchtruck.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrangeLowAlpha
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.White

private val LightColorScheme = lightColorScheme(
    primary = PrimaryOrange,
    secondary = PrimaryOrange,
    tertiary = PrimaryOrange,
    background = PrimaryBackground,
    surface = White,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    surfaceVariant = PrimaryOrangeLowAlpha,
    onSurfaceVariant = PrimaryOrange,

)

private val DarkColorScheme = LightColorScheme

@Composable
fun MunchtruckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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