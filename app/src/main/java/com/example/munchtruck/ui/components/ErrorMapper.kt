package com.example.munchtruck.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R
import com.example.munchtruck.util.MenuItemValidationError
import com.example.munchtruck.viewmodels.AuthError
import com.example.munchtruck.viewmodels.DiscoveryError
import com.example.munchtruck.viewmodels.ProfileError

@Composable
fun AuthError.toMessage(): String = when (this) {
    AuthError.EmptyFields -> stringResource(R.string.error_login_empty_fields)
    AuthError.InvalidEmail -> stringResource(R.string.error_invalid_email)
    AuthError.PasswordTooShort -> stringResource(R.string.error_password_too_short)
    AuthError.PasswordsDoNotMatch -> stringResource(R.string.error_passwords_not_matching)
    AuthError.LoginFailed -> stringResource(R.string.error_login_failed)
    AuthError.RegistrationFailed -> stringResource(R.string.error_registration_failed)

}

@Composable
fun ProfileError.toMessage(): String = when (this) {
    ProfileError.LoadProfileFailed -> stringResource(R.string.error_profile_load_failed)
    ProfileError.UpdateFailed -> stringResource(R.string.error_profile_update_failed)
    ProfileError.EmptyName -> stringResource(R.string.error_profile_name_empty)
    ProfileError.SignOutFailed -> stringResource(R.string.error_logout_failed)
    ProfileError.InvalidTimeInterval -> stringResource(R.string.error_profile_invalid_time_interval)

}

@Composable
fun MenuItemValidationError.toMessage(): String = when (this) {
    MenuItemValidationError.NameEmpty -> stringResource(R.string.error_menu_name_empty)
    MenuItemValidationError.NameTooShort -> stringResource(R.string.error_menu_name_too_short)
    MenuItemValidationError.NameTooLong -> stringResource(R.string.error_menu_name_too_long)
    MenuItemValidationError.NameOnlyNumbers -> stringResource(R.string.error_menu_name_only_numbers)
    MenuItemValidationError.NameInvalidCharacters -> stringResource(R.string.error_menu_name_invalid_characters)
    MenuItemValidationError.DescriptionTooLong -> stringResource(R.string.error_menu_description_too_long)
    MenuItemValidationError.PriceEmpty -> stringResource(R.string.error_menu_price_empty)
    MenuItemValidationError.PriceInvalidFormat -> stringResource(R.string.error_menu_price_invalid_format)
    MenuItemValidationError.PriceMustBeGreaterThanZero -> stringResource(R.string.error_menu_price_must_be_positive)
    MenuItemValidationError.PriceTooHigh -> stringResource(R.string.error_menu_price_too_high)
    MenuItemValidationError.PriceTooManyDecimals -> stringResource(R.string.error_menu_price_max_two_decimals)
    MenuItemValidationError.ImageUrlInvalid -> stringResource(R.string.error_menu_image_invalid_url)

}

@Composable
fun DiscoveryError.toMessage(): String = when (this) {
    DiscoveryError.LocationPermissionDenied -> stringResource(R.string.error_location_permission_denied)
    DiscoveryError.LocationFetchFailed -> stringResource(R.string.error_location_fetch_failed)
    DiscoveryError.LoadTrucksFailed -> stringResource(R.string.error_trucks_load_failed)
    DiscoveryError.LoadMenuFailed -> stringResource(R.string.error_menu_load_failed)
}