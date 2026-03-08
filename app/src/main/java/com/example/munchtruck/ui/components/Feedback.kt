package com.example.munchtruck.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.munchtruck.ui.theme.Dimens.SpaceSM

// ====== Inline Error Message (UI Layer) ===============================

@Composable
fun InlineError(
    message: String,
    modifier: Modifier = Modifier
) {
    if (message.isNotEmpty()) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier
        )
    }
}

// ====== Feedback Snackbar (UI Layer) ===============================

@Composable
fun FeedbackSnackbar(
    message: String?,
    snackbarHostState: SnackbarHostState,
    onConsumed: () -> Unit
) {
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            onConsumed()
        }
    }
}

// ====== Centered Loading Indicator (UI Layer) ===============================

@Composable
fun CenteredLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// ====== Centered Message (UI Layer) ===============================

@Composable
fun CenteredMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// ====== Centered Message With Retry (UI Layer) ===============================

@Composable
fun CenteredMessageWithRetry(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(SpaceSM))
            Button(onClick = onRetry) {
                Text(text = "Retry")
            }
        }
    }
}