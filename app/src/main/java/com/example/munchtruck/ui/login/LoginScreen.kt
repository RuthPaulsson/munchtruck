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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.munchtruck.R
import androidx.navigation.NavController
import com.example.munchtruck.ui.theme.AppColors.DarkOverlay
import com.example.munchtruck.ui.theme.AppColors.InputBorder
import com.example.munchtruck.ui.theme.AppColors.LinkColor
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.TextMuted
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.InputRadius
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
import com.example.munchtruck.viewmodels.AuthViewModel


@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("profile"){
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxWidth()) {

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        stringResource(R.string.input_email_hint),
                        color = TextMuted
                    ) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(InputRadius),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedBorderColor = InputBorder,
                    unfocusedBorderColor = InputBorder,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText,
                    cursorColor = PrimaryText

                )
            )

            Spacer(modifier = Modifier.height(SpaceM))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        stringResource(R.string.input_password_hint),
                        color = TextMuted
                    ) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(InputRadius),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = White,
                    unfocusedContainerColor = White,
                    focusedBorderColor = InputBorder,
                    unfocusedBorderColor = InputBorder,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText,
                    cursorColor = PrimaryText

                )
            )

            Spacer(modifier = Modifier.height(SpaceS))

            Text(
                stringResource(R.string.login_forgot_password),
                color = White,
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable {
                        navController.navigate("forgot_password")
                    }
            )

            Spacer(modifier = Modifier.height(SpaceBetweenLinkAndButton))

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = SpaceS)
                )
            }

            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryOrange
                )
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
                    modifier = Modifier.clickable{
                        navController.navigate("register")
                    }
                )
            }
        }
    }
}
