package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


data class FormState(
    val goalName: String = "",
    val startAt: Long? = null,
    val completedAt: Long? = null,
    val notes: String = "",
    val parameters: List<Parameter> = listOf(),
    val startAtDisplay: String = "",
    val completedAtDisplay: String = ""
)

class GoalFormViewModel(
    val goalRepository: GoalRepository,
    val navigator: Navigator
): ViewModel() {
    private val _formState = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()

    fun addGoal(goal: Goal) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            goalRepository.insertOne(goal)
        }

        navigator.goBack()
    }

    fun onNameChanges(name: String) {
        _formState.update { state ->
            state.copy(
                goalName = name
            )
        }
    }

    fun onStartDateChanges(dateMillis: Long?) {
        if (dateMillis == null) return
        _formState.update { state ->
            val tz = TimeZone.currentSystemDefault()
            val selectedDate = kotlin.time.Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(tz).date
            val newDateTime = if (state.startAt != null) {
                val current = kotlin.time.Instant.fromEpochMilliseconds(state.startAt).toLocalDateTime(tz)
                LocalDateTime(selectedDate, current.time)
            } else {
                val nowMillis = Clock.System.now().toEpochMilliseconds()
                val now = kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz)
                LocalDateTime(selectedDate, now.time)
            }
            val newStartAt = newDateTime.toInstant(tz).toEpochMilliseconds()
            state.copy(
                startAt = newStartAt,
                startAtDisplay = formatDateTime(newStartAt)
            )
        }
    }

    fun onStartTimeChanges(hour: Int, minute: Int) {
        _formState.update { state ->
            val tz = TimeZone.currentSystemDefault()
            val newDateTime = if (state.startAt != null) {
                val current = kotlin.time.Instant.fromEpochMilliseconds(state.startAt).toLocalDateTime(tz)
                LocalDateTime(current.date, LocalTime(hour, minute, 0))
            } else {
                val todayMillis = Clock.System.now().toEpochMilliseconds()
                val today = kotlin.time.Instant.fromEpochMilliseconds(todayMillis).toLocalDateTime(tz).date
                LocalDateTime(today, LocalTime(hour, minute, 0))
            }
            val newStartAt = newDateTime.toInstant(tz).toEpochMilliseconds()
            state.copy(
                startAt = newStartAt,
                startAtDisplay = formatDateTime(newStartAt)
            )
        }
    }

    fun onCompletedDateChanges(dateMillis: Long?) {
        if (dateMillis == null) return
        _formState.update { state ->
            val tz = TimeZone.currentSystemDefault()
            val selectedDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(tz).date
            val newDateTime = if (state.completedAt != null) {
                val current = kotlin.time.Instant.fromEpochMilliseconds(state.completedAt).toLocalDateTime(tz)
                LocalDateTime(selectedDate, current.time)
            } else {
                val nowMillis = Clock.System.now().toEpochMilliseconds()
                val now = Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz)
                LocalDateTime(selectedDate, now.time)
            }
            val newCompletedAt = newDateTime.toInstant(tz).toEpochMilliseconds()
            state.copy(
                completedAt = newCompletedAt,
                completedAtDisplay = formatDateTime(newCompletedAt)
            )
        }
    }

    fun onCompletedTimeChanges(hour: Int, minute: Int) {
        _formState.update { state ->
            val tz = TimeZone.currentSystemDefault()
            val newDateTime = if (state.completedAt != null) {
                val current = Instant.fromEpochMilliseconds(state.completedAt).toLocalDateTime(tz)
                LocalDateTime(current.date, LocalTime(hour, minute, 0))
            } else {
                val todayMillis = Clock.System.now().toEpochMilliseconds()
                val today = kotlin.time.Instant.fromEpochMilliseconds(todayMillis).toLocalDateTime(tz).date
                LocalDateTime(today, LocalTime(hour, minute, 0))
            }
            val newCompletedAt = newDateTime.toInstant(tz).toEpochMilliseconds()
            state.copy(
                completedAt = newCompletedAt,
                completedAtDisplay = formatDateTime(newCompletedAt)
            )
        }
    }

    fun onNotesChanges(notes: String) {
        _formState.update { state ->
            state.copy(
                notes = notes
            )
        }
    }
}

private fun formatDateTime(epochMillis: Long): String {
    val tz = TimeZone.currentSystemDefault()
    val dt = kotlin.time.Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz)
    val amPm = if (dt.hour < 12) "AM" else "PM"
    val hour12 = when {
        dt.hour == 0 -> 12
        dt.hour > 12 -> dt.hour - 12
        else -> dt.hour
    }
    val monthAbbr = dt.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    return "$monthAbbr ${dt.day}, ${dt.year} ${hour12}:${dt.minute.toString().padStart(2, '0')} $amPm"
}
