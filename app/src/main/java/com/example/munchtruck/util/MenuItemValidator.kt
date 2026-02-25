package com.example.munchtruck.util

object MenuItemValidator {

    sealed class MenuItemValidationError {
        data object NameEmpty : MenuItemValidationError()
        data object NameTooShort : MenuItemValidationError()
        data object NameTooLong : MenuItemValidationError()
        data object NameOnlyNumbers : MenuItemValidationError()
        data object NameInvalidCharacters : MenuItemValidationError()

        data object DescriptionTooLong : MenuItemValidationError()

        data object PriceEmpty : MenuItemValidationError()
        data object PriceInvalidFormat : MenuItemValidationError()
        data object PriceMustBeGreaterThanZero : MenuItemValidationError()
        data object PriceTooHigh : MenuItemValidationError()
        data object PriceTooManyDecimals : MenuItemValidationError()

        data object ImageUrlInvalid : MenuItemValidationError()
    }

    fun validateName(name: String): MenuItemValidationError? {
        val trimmedName = name.trim()

        return when {
            trimmedName.isBlank() -> MenuItemValidationError.NameEmpty
            trimmedName.length < 2 -> MenuItemValidationError.NameTooShort
            trimmedName.length > 50 -> MenuItemValidationError.NameTooLong
            trimmedName.matches(Regex("^[0-9\\s]+$")) -> MenuItemValidationError.NameOnlyNumbers
            !trimmedName.matches(Regex("^[a-zA-ZåäöÅÄÖ0-9\\s\\-']+$")) -> MenuItemValidationError.NameInvalidCharacters
            else -> null
        }
    }

    fun validateDescription(description: String): MenuItemValidationError? {
        return if (description.length > 200) {
            MenuItemValidationError.DescriptionTooLong
        } else {
            null
        }
    }

    fun validatePrice(priceString: String): MenuItemValidationError? {
        val trimmedPrice = priceString.trim()
        if (trimmedPrice.isBlank()) return MenuItemValidationError.PriceEmpty

        val normalizedPrice = trimmedPrice.replace(",", ".")
        val price =
            normalizedPrice.toDoubleOrNull() ?: return MenuItemValidationError.PriceInvalidFormat

        return when {
            price <= 0 -> MenuItemValidationError.PriceMustBeGreaterThanZero
            price > 9999.99 -> MenuItemValidationError.PriceTooHigh
            normalizedPrice.contains(".") && normalizedPrice.split(".").last()
                .length > 2 -> MenuItemValidationError.PriceTooManyDecimals

            else -> null


        }
    }

    fun validateImageUrl(imageUrl: String): MenuItemValidationError? {
        val trimmedImageUrl = imageUrl.trim()
        
        return if (trimmedImageUrl.isBlank() &&
            !trimmedImageUrl.matches(Regex("^(http|https)://.*$"))) {
            MenuItemValidationError.ImageUrlInvalid
        } else {
            null
        }
    }

}