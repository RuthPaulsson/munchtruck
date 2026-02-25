package com.example.munchtruck.util

data class MenuItemFieldErrors(
    val name: MenuItemValidator? = null,
    val price: MenuItemValidator? = null,
    val description: MenuItemValidator? = null,
    val imageUrl: MenuItemValidator? = null,
) {
    fun hasErrors(): Boolean = name != null || price != null ||
            description != null || imageUrl != null
}
