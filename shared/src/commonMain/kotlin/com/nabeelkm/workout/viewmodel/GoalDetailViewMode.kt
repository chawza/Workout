package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.Screen
import com.nabeelkm.workout.dao.GoalWithParameter
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GoalDetailUIState {
    data class Success(val goalWithParameter: GoalWithParameter): GoalDetailUIState()
    class Failed: GoalDetailUIState()
    class Idle: GoalDetailUIState()
}

class GoalDetailViewModel(
    val goalId: Int,
    val repository: GoalRepository,
    public val navigator: Navigator,
): ViewModel() {
    val uiState = repository.getByIdWithParametersFlow(goalId)
        .map { goalWithParameter ->
            if (goalWithParameter == null) {
                GoalDetailUIState.Failed()
            } else {
                GoalDetailUIState.Success(goalWithParameter)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GoalDetailUIState.Idle()
        )

    fun switchState(state: GoalStatus) {
        if (uiState.value is GoalDetailUIState.Success) {
            val goalWithParameter = (uiState.value as GoalDetailUIState.Success).goalWithParameter
            viewModelScope.launch {
                repository.updateStatus(goalWithParameter.goal, state)
            }
        }
    }

    fun navigateToEditScreen() {
        if (uiState.value is GoalDetailUIState.Success) {
            val goalWithParameter = (uiState.value as GoalDetailUIState.Success).goalWithParameter
            navigator.navigate(Screen.GoalEditForm(goalWithParameter.goal.id))
        }
    }
}