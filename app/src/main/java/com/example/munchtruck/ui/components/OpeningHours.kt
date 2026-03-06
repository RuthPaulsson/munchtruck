package com.example.munchtruck.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.munchtruck.R
import com.example.munchtruck.data.model.OpeningHours
import com.example.munchtruck.data.model.OpeningInterval
import com.example.munchtruck.ui.theme.Dimens.DayLabelWidth
import com.example.munchtruck.ui.theme.Dimens.SpaceSM
import com.example.munchtruck.ui.theme.Dimens.SpaceS
import com.example.munchtruck.ui.theme.Dimens.SpaceXS
import com.example.munchtruck.ui.theme.Dimens.StatusButtonWidth
import com.example.munchtruck.ui.theme.Dimens.TextSizeXS
import com.example.munchtruck.ui.theme.Dimens.TimeInputWidth

// ====== Opening Hours Section (UI Layer) ===============================

@Composable
fun OpeningHoursSection(
    openingHours: OpeningHours,
    onOpeningHoursChange: ((String, OpeningInterval?) -> Unit)? = null,
    isReadOnly: Boolean = false
) {
    val days = listOf(
        Triple("mon", R.string.opening_hours_day_mon, openingHours.weekly.mon),
        Triple("tue", R.string.opening_hours_day_tue, openingHours.weekly.tue),
        Triple("wed", R.string.opening_hours_day_wed, openingHours.weekly.wed),
        Triple("thu", R.string.opening_hours_day_thu, openingHours.weekly.thu),
        Triple("fri", R.string.opening_hours_day_fri, openingHours.weekly.fri),
        Triple("sat", R.string.opening_hours_day_sat, openingHours.weekly.sat),
        Triple("sun", R.string.opening_hours_day_sun, openingHours.weekly.sun),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        days.forEach { (key, labelRes, interval) ->
            DayInputRow(
                dayName = stringResource(labelRes),
                interval = interval,
                isReadOnly = isReadOnly,
                onIntervalChange = { newInterval ->
                    onOpeningHoursChange?.invoke(key, newInterval)
                }
            )
        }
    }
}

// ====== Day Input Row (UI Layer) ===============================

@Composable
fun DayInputRow(
    dayName: String,
    interval: OpeningInterval?,
    onIntervalChange: ((OpeningInterval?) -> Unit)? = null,
    isReadOnly: Boolean = false
) {
    val startPlaceholder = stringResource(R.string.opening_hours_placeholder_start)
    val endPlaceholder = stringResource(R.string.opening_hours_placeholder_end)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpaceS),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.width(DayLabelWidth)
        )

        if (isReadOnly) {
            Text(
                text = if (interval == null) stringResource(R.string.status_label_closed)
                else "${interval.start} - ${interval.end}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (interval == null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = SpaceSM)
            )
        } else {
            TextButton(
                onClick = {
                    if (interval == null) {
                        onIntervalChange?.invoke(OpeningInterval(startPlaceholder, endPlaceholder))
                    } else {
                        onIntervalChange?.invoke(null)
                    }
                },
                modifier = Modifier.width(StatusButtonWidth)
            ) {
                Text(
                    text = if (interval == null) stringResource(R.string.status_label_closed)
                    else stringResource(R.string.status_label_open),
                    color = if (interval == null) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.width(SpaceS))

            if (interval != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SpaceXS)
                ) {
                    InputField(
                        value = interval.start,
                        onChange = { onIntervalChange?.invoke(interval.copy(start = it)) },
                        placeholder = startPlaceholder,
                        modifier = Modifier.width(TimeInputWidth),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = TextSizeXS)
                    )
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    InputField(
                        value = interval.end,
                        onChange = { onIntervalChange?.invoke(interval.copy(end = it)) },
                        placeholder = endPlaceholder,
                        modifier = Modifier.width(TimeInputWidth),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = TextSizeXS)
                    )
                }
            }
        }
    }
}

// ====== Opening Hours Helpers (Logic Layer) ===============================

@Composable
fun OpeningHours.toDisplayString(): String {
    if (this.tempClosed) return stringResource(R.string.status_label_closed)
    return stringResource(R.string.opening_hours_title)
}

fun updateOpeningHoursState(
    current: OpeningHours,
    day: String,
    interval: OpeningInterval?
): OpeningHours {
    val updatedWeekly = when (day) {
        "mon" -> current.weekly.copy(mon = interval)
        "tue" -> current.weekly.copy(tue = interval)
        "wed" -> current.weekly.copy(wed = interval)
        "thu" -> current.weekly.copy(thu = interval)
        "fri" -> current.weekly.copy(fri = interval)
        "sat" -> current.weekly.copy(sat = interval)
        "sun" -> current.weekly.copy(sun = interval)
        else -> current.weekly
    }
    return current.copy(weekly = updatedWeekly)
}

fun OpeningHours.isOpenNow(): Boolean {
    if (this.tempClosed) return false

    val now = java.time.LocalTime.now()
    val today = java.time.LocalDate.now().dayOfWeek.name.lowercase().take(3)

    val interval = when(today) {
        "mon" -> weekly.mon
        "tue" -> weekly.tue
        "wed" -> weekly.wed
        "thu" -> weekly.thu
        "fri" -> weekly.fri
        "sat" -> weekly.sat
        "sun" -> weekly.sun
        else -> null
    } ?: return false

    return try {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("H:mm")
        val start = java.time.LocalTime.parse(interval.start.trim(), formatter)
        val end = java.time.LocalTime.parse(interval.end.trim(), formatter)

        now.isAfter(start) && now.isBefore(end)
    } catch (e: Exception) {
        false
    }
}
