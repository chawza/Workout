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


data class FormState(
    val goalName: String = "",
    val startAt: Long? = null,
    val notes: String = "",
    val parameters: List<Parameter> = listOf(),
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

    fun onStartAtChanges(startAt: Long?) {
        _formState.update { state ->
            state.copy(
                startAt = startAt
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