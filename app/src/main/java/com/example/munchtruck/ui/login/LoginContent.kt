package com.example.munchtruck.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.DarkAuthBackground
import com.example.munchtruck.ui.components.AuthHeader
import com.example.munchtruck.ui.components.InlineError
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS

@Composable
fun LoginContent(
    email: String,
    password: String,
    error: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    DarkAuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Lagom avstånd i toppen så loggan hamnar bra
            Spacer(modifier = Modifier.height(SpaceL))

            // AuthHeader utan subtitle
            AuthHeader(subtitle = "")

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Input Fields (Utan labels) =====================

            InputField(
                value = email,
                onChange = onEmailChange,
                label = "", // Ingen label här
                placeholder = stringResource(R.string.input_email_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = password,
                onChange = onPasswordChange,
                label = "", // Ingen label här
                placeholder = stringResource(R.string.input_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceS))

            // Forgot Password Link
            Text(
                text = stringResource(R.string.login_forgot_password),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onForgotPasswordClick() }
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Error Messages ===============================

            if (error.isNotEmpty()) {
                InlineError(
                    message = error,
                    modifier = Modifier.padding(bottom = SpaceS)
                )
            }

            // ====== Actions ===============================

            Button(
                onClick = onLoginClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = LoaderStroke,
                        modifier = Modifier.size(LoaderSize)
                    )
                } else {
                    Text(text = stringResource(R.string.login_button))
                }
            }

            Spacer(modifier = Modifier.height(SpaceM))

            // Registration Link
            Row {
                Text(
                    text = stringResource(R.string.login_no_account),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.login_create_account),
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .padding(start = SpaceS)
                        .clickable { onRegisterClick() }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginContentPreview() {
    AppPreviewWrapper {
        LoginContent(
            email = "",
            password = "",
            error = "",
            isLoading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}