package com.example.munchtruck.util

import java.text.NumberFormat
import java.util.Locale

// ====== Price Formatter Utility ===============================

object PriceFormatter {

    // ====== Configuration ===============================

    private val swedishCurrencyFormat by lazy {
        NumberFormat.getCurrencyInstance(Locale("sv", "SE")).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }
    }

    // ====== Formatting (Long/Double to String) ==============

    fun format(priceInOre: Long): String {
        val priceInKronor = priceInOre / 100.0
        return try {
            swedishCurrencyFormat.format(priceInKronor)
        } catch (e: Exception) {
            "%.2f kr".format(priceInKronor).replace(".", ",")
        }
    }

    fun format(priceInKronor: Double): String {
        return try {
            swedishCurrencyFormat.format(priceInKronor)
        } catch (e: Exception) {
            "%.2f kr".format(priceInKronor).replace(".", ",")
        }
    }

    // ====== Parsing (String to Long/Double) =====================

    fun parseToOre(priceString: String): Long? {
        return try {
            val cleaned = priceString
                .replace("kr", "")
                .replace(" ", "")
                .replace(",", ".")
                .trim()
            val kronor = cleaned.toDoubleOrNull() ?: return null
            (kronor * 100).toLong()  // Convert to öre
        } catch (e: Exception) {
            null
        }
    }

    fun parseToKronor(priceString: String): Double? {
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

    // ====== Extension Functions ===============================

    fun Long.toPriceString(): String = format(this)

    fun Double.toPriceString(): String = format(this)
}