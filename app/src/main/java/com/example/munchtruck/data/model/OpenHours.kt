package com.example.munchtruck.data.model

data class OpeningHours (
    val timeZone: String = "",
    val weekly: WeeklyOpeningHours = WeeklyOpeningHours(),
    val tempClosed: Boolean = false
)

data class WeeklyOpeningHours (
        val mon: OpeningInterval? = null,
        val tue: OpeningInterval? = null,
        val wed: OpeningInterval? = null,
        val thu: OpeningInterval? = null,
        val fri: OpeningInterval? = null,
        val sat: OpeningInterval? = null,
        val sun: OpeningInterval? = null
)


data class OpeningInterval(
    val start: String = "",
    val end: String = ""

)

