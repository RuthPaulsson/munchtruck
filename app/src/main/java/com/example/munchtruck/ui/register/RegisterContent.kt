package com.example.munchtruck.ui.register

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.AuthBackground
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

// ====== Register Content (UI Layer) ===============================

@Composable
fun RegisterContent(
    company: String,
    email: String,
    password: String,
    confirmPassword: String,
    error: String,
    isLoading: Boolean,
    onCompanyChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ====== Header Section ===============================

            AuthHeader(
                subtitle = stringResource(R.string.register_title_subtitle)
            )

            // ====== Input Form Section ===============================

            InputField(
                value = company,
                onChange = onCompanyChange,
                label = stringResource(R.string.register_label_company),
                placeholder = stringResource(R.string.register_hint_company)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = email,
                onChange = onEmailChange,
                label = stringResource(R.string.register_label_email),
                placeholder = stringResource(R.string.common_input_hint_email)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = password,
                onChange = onPasswordChange,
                label = stringResource(R.string.register_label_password),
                placeholder = stringResource(R.string.common_input_hint_password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = confirmPassword,
                onChange = onConfirmPasswordChange,
                label = stringResource(R.string.register_label_password_confirm),
                placeholder = stringResource(R.string.common_input_hint_password_confirm),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Error Messages ===============================

            InlineError(
                message = error,
                modifier = Modifier.padding(bottom = SpaceS)
            )

            // ====== Actions ===============================

            Button(
                onClick = onRegisterClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = LoaderStroke,
                        modifier = Modifier.size(LoaderSize)
                    )
                } else {
                    Text(text = stringResource(R.string.register_button_get_started))
                }
            }
        }
    }
}

// ====== Preview ===============================

@Preview(showBackground = true)
@Composable
fun RegisterContentPreview() {
    AppPreviewWrapper {
        RegisterContent(
            company = "",
            email = "",
            password = "",
            confirmPassword = "",
            error = "",
            isLoading = false,
            onCompanyChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onRegisterClick = {}
        )
    }
}
