package com.example.munchtruck.ui.forgot

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
import androidx.compose.ui.text.style.TextAlign
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

// ====== Forgot Password Content (UI Layer) ===============================

@Composable
fun ForgotPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onResetClick: () -> Unit,
    isLoading: Boolean,
    error: String,
    isSuccess: Boolean
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
                subtitle = stringResource(R.string.forgot_password_title_subtitle)
            )

            if (isSuccess) {

                // ====== Success State ===============================

                Text(
                    text = stringResource(R.string.forgot_password_message_success),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = SpaceM)
                )
            } else {

                // ====== Input Form ===============================

                InputField(
                    value = email,
                    onChange = onEmailChange,
                    label = stringResource(R.string.register_label_email),
                    placeholder = stringResource(R.string.common_input_hint_email)
                )

                Spacer(modifier = Modifier.height(SpaceL))

                InlineError(
                    message = error,
                    modifier = Modifier.padding(bottom = SpaceS)
                )

                Button(
                    onClick = onResetClick,
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
                        Text(text = stringResource(R.string.forgot_password_button_send_link))
                    }
                }
            }
        }
    }
}

// ====== Preview ===============================

@Preview(showBackground = true)
@Composable
fun ForgotPasswordContentPreview() {
    AppPreviewWrapper {
        ForgotPasswordContent(
            email = "",
            onEmailChange = {},
            onResetClick = {},
            isLoading = false,
            error = "",
            isSuccess = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordSuccessPreview() {
    AppPreviewWrapper {
        ForgotPasswordContent(
            email = "user@example.com",
            onEmailChange = {},
            onResetClick = {},
            isLoading = false,
            error = "",
            isSuccess = true
        )
    }
}
