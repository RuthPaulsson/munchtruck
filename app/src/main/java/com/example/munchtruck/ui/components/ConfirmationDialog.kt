package com.example.munchtruck.ui.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R

@Composable
fun ConfirmationDialog(
    show: Boolean,
    onDismiss: ()-> Unit,
    onConfirm: () -> Unit,
    title: String,
    message: String,
    confirmText: String = stringResource(R.string.delete_confirm),
    dismissText: String = stringResource(R.string.common_cancel),
    isDangerous: Boolean = false
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    colors = if (isDangerous) {
                        ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)

                    } else {
                        ButtonDefaults.textButtonColors(contentColor = Black)
                    }
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(dismissText)
                }
            }
        )
    }
}