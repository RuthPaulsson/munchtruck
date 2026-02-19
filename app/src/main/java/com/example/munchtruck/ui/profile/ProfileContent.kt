package com.example.munchtruck.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.SpaceL

@Composable
fun ProfileContent (
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit
) {
    // ====== Layout ===============================

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Welcome!")

            Spacer(modifier = Modifier.height(SpaceL))

            Button(
                onClick = onEditClick
            ) {
                Text(("Edit profile"))
            }

            Spacer(modifier = Modifier.height(SpaceL))

            Button(
                onClick = onLogoutClick
            ) {
                Text("Log out")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    AppPreviewWrapper() {
        ProfileContent(
            onLogoutClick = {},
            onEditClick = {}
        )
    }
}