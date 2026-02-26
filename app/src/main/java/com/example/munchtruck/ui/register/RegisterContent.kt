package com.example.munchtruck.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.InlineError
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.AppPreviewWrapper
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.LogoHeightSmall
import com.example.munchtruck.ui.theme.Dimens.LogoWidthSmall
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS


// ====== Register Content ===============================

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
){

    // ====== Root Container ===============================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackground)
    ) {

        Image(
            painter = painterResource(R.drawable.basic_car),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth

        )

        // ====== Main Content ===============================

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(SpaceL))

            Image(
                painter = painterResource(R.drawable.munchtruck_text),
                contentDescription = stringResource(R.string.logo_munchtruck),
                modifier = Modifier
                    .fillMaxWidth(LogoWidthSmall)
                    .height(LogoHeightSmall),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(SpaceS))

            Text(
                text = stringResource(R.string.register_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Input Fields ===============================

            InputField(
                value = company,
                onChange = onCompanyChange,
                lable = stringResource(R.string.register_company_label),
                placeholder = stringResource(R.string.register_company_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = email,
                onChange = onEmailChange,
                lable = stringResource(R.string.register_email_label),
                placeholder = stringResource(R.string.input_email_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = password,
                onChange = onPasswordChange,
                lable = stringResource(R.string.register_password_label),
                placeholder = stringResource(R.string.input_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = confirmPassword,
                onChange = onConfirmPasswordChange,
                lable = stringResource(R.string.register_confirm_label),
                placeholder = stringResource(R.string.input_confirm_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceL))

            // ====== Error Messages ===============================

            InlineError(
                message = error,
                modifier = Modifier.padding(bottom = SpaceS)
            )

            // ====== Register Button ===============================

            Button(
                onClick = onRegisterClick,
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
                    Text(stringResource(R.string.register_button))

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