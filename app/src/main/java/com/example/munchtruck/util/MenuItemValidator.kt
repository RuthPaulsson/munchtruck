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

    fun validateName(name: String): String? {
        return when {
            name.isBlank() -> "Namn kan inte vara tomt"
            name.length < 2 -> "Namnet måste vara minst 2 tecken"
            name.length > 50 -> "Namnet kan vara max 50 tecken"
            name.matches(Regex("^[0-9\\s]+$")) -> "Namnet kan inte bara innehålla siffror"
            !name.matches(Regex("^[a-zA-ZåäöÅÄÖ0-9\\s\\-']+$")) -> "Namnet innehåller ogiltiga tecken"
            else -> null
        }
    }

    fun validateDescription(description: String): String? {
        return when {
            description.length > 200 -> "Beskrivningen kan vara max 200 tecken"
            else -> null
        }
    }

    fun validatePrice(priceString: String): String? {
        return when {
            priceString.isBlank() -> "Pris kan inte vara tomt"
            else -> {
                val price = priceString.replace(",", ".").toDoubleOrNull()
                when {
                    price == null -> "Ogiltigt prisformat"
                    price <= 0 -> "Priset måste vara större än 0"
                    price > 9999.99 -> "Priset kan vara max 9 999,99 kr"
                    priceString.contains(".") && priceString.split(".").last().length > 2 ->
                        "Max 2 decimaler"
                    else -> null
                }
            }
        }
    }

    fun validateImageUrl(imageUrl: String): String? {
        return when {
            imageUrl.isNotBlank() && !imageUrl.matches(Regex("^(http|https)://.*$")) ->
                "Ogiltig URL. Måste börja med http:// eller https://"
            else -> null
        }
    }

    fun validateAll(
        name: String,
        priceString: String,
        description: String = "",
        imageUrl: String = ""
    ): Boolean {
        return validateName(name) == null &&
                validatePrice(priceString) == null &&
                validateDescription(description) == null &&
                validateImageUrl(imageUrl) == null
    }
}