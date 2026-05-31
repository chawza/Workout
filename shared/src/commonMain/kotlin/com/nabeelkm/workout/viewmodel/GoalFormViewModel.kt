package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant


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
): ViewModel() {
    private val _goalId = MutableStateFlow<Int?>(null)
    private val _formState = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()
    private val _existingGoal = MutableStateFlow<Goal?>(null)
    val existingGoal = _existingGoal.asStateFlow()

    fun loadGoal(id: Int) {
        _goalId.update { id }
        loadExistingGoal()
    }

    fun resetStates() {
        _goalId.update { null }

        _formState.update {
            FormState()
        }
    }

    private fun loadExistingGoal() {
        val id = _goalId.value ?: return
        viewModelScope.launch {
            val goal = withContext(Dispatchers.IO) {
                goalRepository.getById(id)
            }
            if (goal != null) {
                _existingGoal.value = goal
                _formState.update { state ->
                    state.copy(
                        goalName = goal.name,
                        startAt = goal.startAt,
                        completedAt = goal.completedAt,
                        startAtDisplay = goal.startAt?.let { formatDateTime(it) } ?: "",
                        completedAtDisplay = goal.completedAt?.let { formatDateTime(it) } ?: "",
                    )
                }
            }
        }
    }

    fun addGoal() = viewModelScope.launch {
        val formState = _formState.value

        val newGoal = Goal(
            0,
            name = formState.goalName,
            status = GoalStatus.NEW.value,
            createdAt = Clock.System.now().toEpochMilliseconds(),
            completedAt =  formState.completedAt,
            startAt = formState.startAt,
            notes = formState.notes
        )

        withContext(Dispatchers.IO) {
            goalRepository.insertOne(newGoal)
        }
    }
    fun onUpdate() = viewModelScope.launch {
        val goal = _existingGoal.value ?: return@launch
        val state = _formState.value


        val updatedGoal = goal.copy(
            id = goal.id,
            name = state.goalName,
            startAt = state.startAt,
            completedAt = state.completedAt,
            notes = state.notes,
        )

        withContext(Dispatchers.IO) {
            goalRepository.upsert(updatedGoal)
        }
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
            val selectedDate = Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(tz).date
            val newDateTime = if (state.startAt != null) {
                val current = Instant.fromEpochMilliseconds(state.startAt).toLocalDateTime(tz)
                LocalDateTime(selectedDate, current.time)
            } else {
                val nowMillis = Clock.System.now().toEpochMilliseconds()
                val now = Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz)
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
                val current = Instant.fromEpochMilliseconds(state.startAt).toLocalDateTime(tz)
                LocalDateTime(current.date, LocalTime(hour, minute, 0))
            } else {
                val todayMillis = Clock.System.now().toEpochMilliseconds()
                val today = Instant.fromEpochMilliseconds(todayMillis).toLocalDateTime(tz).date
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
                val current = Instant.fromEpochMilliseconds(state.completedAt).toLocalDateTime(tz)
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
                val today = Instant.fromEpochMilliseconds(todayMillis).toLocalDateTime(tz).date
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
