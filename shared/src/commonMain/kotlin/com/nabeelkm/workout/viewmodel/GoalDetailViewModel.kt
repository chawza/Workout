package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.dao.GoalWithParameter
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GoalDetailUIState {
    data class Success(val goalWithParameter: GoalWithParameter): GoalDetailUIState()
    class Failed: GoalDetailUIState()
    class Idle: GoalDetailUIState()
}

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class GoalDetailViewModel(
    val repository: GoalRepository,
): ViewModel() {

    private val _goalId = MutableStateFlow<Int?>(null)
    val goalId: StateFlow<Int?> = _goalId.asStateFlow()

    val uiState = _goalId
        .flatMapLatest { id ->
            if (id == null) {
                flowOf(GoalDetailUIState.Idle())
            } else {
                repository.getByIdWithParametersFlow(id).map { goalWithParameter ->
                    if (goalWithParameter == null) {
                        GoalDetailUIState.Failed()
                    } else {
                        GoalDetailUIState.Success(goalWithParameter)
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GoalDetailUIState.Idle()
        )

    fun loadGoal(id: Int) {
        _goalId.value = id
    }

    fun switchState(state: GoalStatus) {
        if (uiState.value is GoalDetailUIState.Success) {
            val goalWithParameter = (uiState.value as GoalDetailUIState.Success).goalWithParameter
            viewModelScope.launch {
                repository.updateStatus(goalWithParameter.goal, state)
            }
        }
    }

}
