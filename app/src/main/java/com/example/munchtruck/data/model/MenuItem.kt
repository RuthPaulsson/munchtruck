package com.example.munchtruck.data.model
import com.google.firebase.Timestamp
import java.text.NumberFormat
import java.util.Locale

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val foodTruckId: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) {
    fun formattedPrice(): String {
        return try {
            val format = NumberFormat.getCurrencyInstance(Locale("sv", "SE"))
            format.format(price)
        } catch (e: Exception) {
            "%.2f kr".format(price).replace(".", ",")
        }
    }
}