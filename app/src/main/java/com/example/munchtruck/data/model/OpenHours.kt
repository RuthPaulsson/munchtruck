package com.example.munchtruck.data.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

data class OpeningHours(
    val timeZone: String = "",
    val weekly: WeeklyOpeningHours = WeeklyOpeningHours(),
    val tempClosed: Boolean = false
)

fun OpeningHours.isCurrentlyOpen(): Boolean {
    if (this.tempClosed) return false

    val zoneId = ZoneId.of(this.timeZone.ifBlank { "Europe/Stockholm" })
    val now = LocalDateTime.now(zoneId)
    val currentTime = now.toLocalTime()

    val interval = when (now.dayOfWeek) {
        DayOfWeek.MONDAY -> weekly.mon
        DayOfWeek.TUESDAY -> weekly.tue
        DayOfWeek.WEDNESDAY -> weekly.wed
        DayOfWeek.THURSDAY -> weekly.thu
        DayOfWeek.FRIDAY -> weekly.fri
        DayOfWeek.SATURDAY -> weekly.sat
        DayOfWeek.SUNDAY -> weekly.sun
    } ?: return false

    return try {
        val start = LocalTime.parse(interval.start)
        val end = LocalTime.parse(interval.end)
        currentTime.isAfter(start) && currentTime.isBefore(end)
    } catch (e: Exception) {
        false
    }
}

data class WeeklyOpeningHours(
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

