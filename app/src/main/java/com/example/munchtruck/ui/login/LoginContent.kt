package com.example.munchtruck.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.AuthHeader
import com.example.munchtruck.ui.components.DarkAuthBackground
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

// ====== Login Content (UI Layer) ===============================

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

            // ====== Header Section ===============================

            Spacer(modifier = Modifier.height(SpaceL))

            AuthHeader(subtitle = "")

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Input Form ===============================

            InputField(
                value = email,
                onChange = onEmailChange,
                label = "",
                placeholder = stringResource(R.string.common_input_hint_email)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = password,
                onChange = onPasswordChange,
                label = "",
                placeholder = stringResource(R.string.common_input_hint_password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceS))

            // ====== Actions ===============================

            Text(
                text = stringResource(R.string.login_text_forgot_password),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onForgotPasswordClick() }
            )

            Spacer(modifier = Modifier.height(SpaceL))

            if (error.isNotEmpty()) {
                InlineError(
                    message = error,
                    modifier = Modifier.padding(bottom = SpaceS)
                )
            }

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
                    Text(text = stringResource(R.string.login_button_login))
                }
            }

            Spacer(modifier = Modifier.height(SpaceM))

            // ====== Footer Links ===============================

            Row {
                Text(
                    text = stringResource(R.string.login_text_no_account),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.login_text_create_account),
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

// ====== Preview ===============================

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
