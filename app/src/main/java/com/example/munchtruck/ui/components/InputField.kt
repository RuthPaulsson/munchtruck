package com.example.munchtruck.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.munchtruck.ui.theme.AppColors.InputBorder
import com.example.munchtruck.ui.theme.AppColors.PrimaryText
import com.example.munchtruck.ui.theme.AppColors.TextMuted
import com.example.munchtruck.ui.theme.AppColors.White
import com.example.munchtruck.ui.theme.Dimens.InputRadius
import com.example.munchtruck.ui.theme.Dimens.SpaceXS

@Composable
fun InputField(
    value: String,
    onChange: (String) -> Unit,
    lable: String? = null,
    placeholder: String,
    isPassword: Boolean = false
){
    Column(modifier = Modifier.fillMaxWidth()) {
        if (!lable.isNullOrEmpty()) {
            Text(
                text = lable,
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(SpaceXS))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextMuted
                )
            },

            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(InputRadius),
            singleLine = true,
            visualTransformation =
                if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,
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
    }
}