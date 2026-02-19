package com.example.munchtruck.ui.profile

import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.SpaceL

@Composable
fun ProfileContent (
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(SpaceL))

        Text("Truck name")

        Spacer(modifier = Modifier.height(SpaceL))

        Button(
            onClick = onEditClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Edit Profile")
        }

        Spacer(modifier = Modifier.height(SpaceL))
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Log out")
        }
        Spacer(modifier = Modifier.height(SpaceL))
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