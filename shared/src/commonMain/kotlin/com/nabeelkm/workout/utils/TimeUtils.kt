package com.nabeelkm.workout.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun formatDateTime(epochMillis: Long): String {
    val tz = TimeZone.currentSystemDefault()
    val dt = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz)
    val amPm = if (dt.hour < 12) "AM" else "PM"
    val hour12 = when {
        dt.hour == 0 -> 12
        dt.hour > 12 -> dt.hour - 12
        else -> dt.hour
    }
    val monthAbbr = dt.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    return "$monthAbbr ${dt.day}, ${dt.year} ${hour12}:${dt.minute.toString().padStart(2, '0')} $amPm"
}
fun formatDate(epochMillis: Long): String {
    val tz = TimeZone.currentSystemDefault()
    val dt = Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz)
    val monthAbbr = dt.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    return "$monthAbbr ${dt.day}, ${dt.year}"
}
