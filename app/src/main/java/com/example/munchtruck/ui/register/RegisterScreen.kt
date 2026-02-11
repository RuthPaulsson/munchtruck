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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.munchtruck.R
import com.example.munchtruck.ui.components.InputField
import com.example.munchtruck.ui.theme.AppColors.PrimaryBackground
import com.example.munchtruck.ui.theme.AppColors.PrimaryOrange
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.Dimens.ButtonRadius
import com.example.munchtruck.ui.theme.Dimens.LoaderSize
import com.example.munchtruck.ui.theme.Dimens.LoaderStroke
import com.example.munchtruck.ui.theme.Dimens.LogoHeightSmall
import com.example.munchtruck.ui.theme.Dimens.LogoWidthSmall
import com.example.munchtruck.ui.theme.Dimens.ScreenPadding
import com.example.munchtruck.ui.theme.Dimens.SpaceL
import com.example.munchtruck.ui.theme.Dimens.SpaceM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.viewmodels.AuthViewModel


@Composable
fun RegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel
){
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val error by authViewModel.error.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("profile") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

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

            InputField(
                value = company,
                onChange = { company = it },
                lable = stringResource(R.string.register_company_label),
                placeholder = stringResource(R.string.register_company_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = email,
                onChange = { email = it },
                lable = stringResource(R.string.register_email_label),
                placeholder = stringResource(R.string.input_email_hint)
            )

            Spacer(modifier = Modifier.height(SpaceM))


            InputField(
                value = password,
                onChange = { password = it },
                lable = stringResource(R.string.register_password_label),
                placeholder = stringResource(R.string.input_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceM))

            InputField(
                value = confirmPassword,
                onChange = { confirmPassword = it },
                lable = stringResource(R.string.register_confirm_label),
                placeholder = stringResource(R.string.input_confirm_password_hint),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(SpaceL))

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = SpaceS)
                )
            }

            Button(
                onClick =  {
                    authViewModel.register(email, password, confirmPassword)
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
                    Text(stringResource(R.string.register_button))

                }
            }

        }
    }

}
//
//@Preview(showBackground = true)
//@Composable
//fun RegisterScreenPreview() {
//    RegisterScreen(
//        navController = androidx.navigation.compose.rememberNavController(),
//        authViewModel = AuthViewModel()
//    )
//}