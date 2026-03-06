package com.example.munchtruck.ui.theme

import androidx.compose.runtime.Composable

// ====== Preview Helpers (Theme Layer) ===============================

@Composable
fun AppPreviewWrapper(
    content: @Composable () -> Unit
) {
    MunchtruckTheme {
        content()
    }
}