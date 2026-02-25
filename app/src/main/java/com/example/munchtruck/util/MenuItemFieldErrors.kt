package com.example.munchtruck.util

data class MenuItemFieldErrors(
    val name: MenuItemValidator.MenuItemValidationError? = null,
    val price: MenuItemValidator.MenuItemValidationError? = null,
    val description: MenuItemValidator.MenuItemValidationError? = null,
    val imageUrl: MenuItemValidator.MenuItemValidationError? = null,
) {
    fun hasErrors(): Boolean = name != null || price != null ||
            description != null || imageUrl != null
}
