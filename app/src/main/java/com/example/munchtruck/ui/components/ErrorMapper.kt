package com.example.munchtruck.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R
import com.example.munchtruck.util.MenuItemValidationError
import com.example.munchtruck.viewmodels.AuthError
import com.example.munchtruck.viewmodels.DiscoveryError
import com.example.munchtruck.viewmodels.ProfileError

// ====== Auth Error Mapping (UI Layer) ===============================

@Composable
fun AuthError.toMessage(): String = when (this) {
    AuthError.EmptyFields -> stringResource(R.string.login_error_empty_fields)
    AuthError.InvalidEmail -> stringResource(R.string.register_error_invalid_email)
    AuthError.PasswordTooShort -> stringResource(R.string.register_error_password_too_short)
    AuthError.PasswordsDoNotMatch -> stringResource(R.string.register_error_passwords_not_matching)
    AuthError.LoginFailed -> stringResource(R.string.login_error_failed)
    AuthError.RegistrationFailed -> stringResource(R.string.register_error_failed)
    AuthError.UserNotFound -> stringResource(R.string.forgot_password_error_user_not_found)
    AuthError.NetworkError -> stringResource(R.string.forgot_password_error_network)
    AuthError.Unknown -> stringResource(R.string.forgot_password_error_unknown)
}

// ====== Profile Error Mapping (UI Layer) ===============================

@Composable
fun ProfileError.toMessage(): String = when (this) {
    ProfileError.LoadProfileFailed -> stringResource(R.string.profile_error_load_failed)
    ProfileError.UpdateFailed -> stringResource(R.string.profile_error_update_failed)
    ProfileError.EmptyName -> stringResource(R.string.profile_error_name_empty)
    ProfileError.SignOutFailed -> stringResource(R.string.profile_error_sign_out_failed)
    ProfileError.InvalidTimeInterval -> stringResource(R.string.opening_hours_error_invalid_interval)
    ProfileError.DeleteFailed -> stringResource(R.string.edit_profile_error_delete_failed)
    ProfileError.RecentLoginRequired -> stringResource(R.string.edit_profile_error_delete_recent_login)
}

// ====== Menu Item Validation Error Mapping (UI Layer) ===============================

@Composable
fun MenuItemValidationError.toMessage(): String = when (this) {
    MenuItemValidationError.NameEmpty -> stringResource(R.string.menu_error_name_empty)
    MenuItemValidationError.NameTooShort -> stringResource(R.string.menu_error_name_too_short)
    MenuItemValidationError.NameTooLong -> stringResource(R.string.menu_error_name_too_long)
    MenuItemValidationError.NameOnlyNumbers -> stringResource(R.string.menu_error_name_only_numbers)
    MenuItemValidationError.NameInvalidCharacters -> stringResource(R.string.menu_error_name_invalid_chars)
    MenuItemValidationError.DescriptionTooLong -> stringResource(R.string.menu_error_description_too_long)
    MenuItemValidationError.PriceEmpty -> stringResource(R.string.menu_error_price_empty)
    MenuItemValidationError.PriceInvalidFormat -> stringResource(R.string.menu_error_price_invalid_format)
    MenuItemValidationError.PriceMustBeGreaterThanZero -> stringResource(R.string.menu_error_price_must_be_positive)
    MenuItemValidationError.PriceTooHigh -> stringResource(R.string.menu_error_price_too_high)
    MenuItemValidationError.PriceTooManyDecimals -> stringResource(R.string.menu_error_price_max_decimals)
    MenuItemValidationError.ImageUrlInvalid -> stringResource(R.string.menu_error_image_url_invalid)
}

// ====== Discovery Error Mapping (UI Layer) ===============================

@Composable
fun DiscoveryError.toMessage(): String = when (this) {
    DiscoveryError.LocationPermissionDenied -> stringResource(R.string.location_error_permission_denied)
    DiscoveryError.LocationFetchFailed -> stringResource(R.string.location_error_fetch_failed)
    DiscoveryError.LoadTrucksFailed -> stringResource(R.string.discovery_error_load_trucks_failed)
    DiscoveryError.LoadMenuFailed -> stringResource(R.string.discovery_error_load_menu_failed)
}
