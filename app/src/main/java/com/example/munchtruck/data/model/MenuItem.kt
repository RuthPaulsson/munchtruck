package com.example.munchtruck.data.model

import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.util.Locale

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val price: Long = 0,
    val description: String = "",
    val imageUrl: String = "",
    val foodTruckId: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun formattedPrice(): String {
        val priceInKronor = price / 100.0
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("sv", "SE"))
            format.format(priceInKronor)
        } catch (e: Exception) {
            "%.2f kr".format(priceInKronor).replace(".", ",")
        }
    }

    fun priceInKronor(): Double = price / 100.0
}