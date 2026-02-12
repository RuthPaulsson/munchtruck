package com.example.munchtruck.ui.theme

import androidx.compose.runtime.Composable

@Composable
fun AppPreviewWrapper (
    content: @Composable () -> Unit
){
    MunchtruckTheme {
        content()
    }
}