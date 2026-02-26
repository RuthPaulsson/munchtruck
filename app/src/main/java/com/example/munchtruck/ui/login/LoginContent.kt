package com.example.munchtruck.ui.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.InlineError
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppColors.DarkOverlay
import com.example.munchtruck.ui.theme.AppColors.LinkColor
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.LoginTopSpacing
import com.example.munchtruck.ui.theme.Dimens.LogoHeightSmall
import com.example.munchtruck.ui.theme.Dimens.LogoWidthSmall
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceAfterButton
import com.example.munchtruck.ui.theme.Dimens.SpaceBetweenLinkAndButton
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS

// ====== Login Content ===============================
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

    // ====== Root Container ===============================

    Box(modifier = Modifier
        .fillMaxWidth()) {

        // ====== Background ===============================

        Image(
            painter = painterResource(R.drawable.bg_foodtruck),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkOverlay)
        )

        // ====== Main Content ===============================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ====== Header ===============================

            Spacer(modifier = Modifier.height(LoginTopSpacing))

            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.logo_munchtruck),
                modifier = Modifier
                    .fillMaxWidth(LogoWidthSmall)
                    .height(LogoHeightSmall),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(SpaceL))

            InputField(
                value = email,
                onChange = onEmailChange,
                lable = "",
                placeholder = stringResource(R.string.input_email_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            // ====== Login Form ===============================

            InputField(
                value = password,
                lable = "",
                onChange = onPasswordChange,
                placeholder = stringResource(R.string.input_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceS))

            // ====== Forgot Password ===============================

            Text(
                stringResource(R.string.login_forgot_password),
                color = White,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable { onForgotPasswordClick() }
            )

            Spacer(modifier = Modifier.height(SpaceBetweenLinkAndButton))

            // ====== Error Message ===============================

            InlineError(
                message = error,
                modifier = Modifier.padding(bottom = SpaceS)
            )

            // ====== Login Button ===============================

            Button(
                onClick = onLoginClick,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius),
            ) {
                if (isLoading){
                    CircularProgressIndicator(
                        color = White,
                        strokeWidth = LoaderStroke,
                        modifier = Modifier.size(LoaderSize)
                    )
                } else {
                    Text(stringResource(R.string.login_button))
                }
            }

            Spacer(modifier = Modifier.height(SpaceAfterButton))

            // ====== Register Link ===============================

            Row{
                Text(
                    stringResource(R.string.login_no_account),
                    color = White,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    stringResource(R.string.login_create_account),
                    color = LinkColor,
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable{ onRegisterClick() }
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
