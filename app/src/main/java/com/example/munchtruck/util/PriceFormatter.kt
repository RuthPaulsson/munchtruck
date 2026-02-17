package com.example.munchtruck.util

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {

    private val swedishCurrencyFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("sv", "SE")).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
    }

    fun format(price: Double): String {
        return try {
            swedishCurrencyFormat.format(price)
        } catch (e: Exception) {
            "%.2f kr".format(price).replace(".", ",")
        }
    }

    fun parse(priceString: String): Double? {
        return try {
            val cleaned = priceString
                .replace("kr", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim()
            cleaned.toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }


    fun Double.toPriceString(): String = format(this)
}