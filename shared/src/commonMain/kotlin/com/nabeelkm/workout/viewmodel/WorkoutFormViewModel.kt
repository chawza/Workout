package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.repository.WorkoutRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutFormState(
    val goal: Goal? = null,
    val time: Long? = null,
    val duration: Long? = null,
    val notes: String? = "",
)


class WorkoutFormViewModel(
    val workoutRepository: WorkoutRepository,
    val goalRepository: GoalRepository
): ViewModel() {
    sealed class UIEvent {
        data object Idle: UIEvent()
        data class SuccessAdd(val workout: Workout): UIEvent()
        data class SuccessUpdate(val workout: Workout): UIEvent()
    }

    private val _uiChannel = Channel<UIEvent>()
    val uiChannelFlow = _uiChannel.receiveAsFlow()


    private val _formState  = MutableStateFlow(WorkoutFormState())
    val formStateFlow = _formState.asStateFlow()

    val selectableGoalsFlow = goalRepository.activeGoals

    fun resetState() {
        _formState.update {
            WorkoutFormState()
        }
    }

    fun onGoalUpdate(goal: Goal) {
        _formState.update {
            _formState.value.copy(
                goal = goal
            )
        }
    }

    fun onTimeUpdate(time: Long) {
        _formState.update {
            _formState.value.copy(
                time = time
            )
        }
    }
    fun onDuration(duration: Long?) {
        _formState.update {
            _formState.value.copy(
                duration = duration
            )
        }
    }
    fun onNotesUpdate(notes: String) {
        _formState.update {
            _formState.value.copy(
                notes = notes
            )
        }
    }

    fun onAddForm() = viewModelScope.launch {
        val form = _formState.value
        if (form.goal == null || form.time == null) {
            // TODO: show error
            return@launch
        }

        if (!selectableGoalsFlow.last().contains(form.goal)) {
            // TODO: show error
            return@launch
        }

        var newWorkout = Workout(
            id = 0,
            goalId = form.goal.id,
            time = form.time,
            duration = form.duration,
            notes = form.notes ?: ""
        )

        val insertedID = workoutRepository.insertOne(newWorkout)
        newWorkout = newWorkout.copy(id = insertedID)
        _uiChannel.send(UIEvent.SuccessAdd(newWorkout))
        resetState()
    }
}