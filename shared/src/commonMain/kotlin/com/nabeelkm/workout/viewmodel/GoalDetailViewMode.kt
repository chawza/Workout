package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class GoalDetailUIState {
    data class Success(val goal: Goal): GoalDetailUIState()
    class Failed: GoalDetailUIState()
    class Idle: GoalDetailUIState()
}

class GoalDetailViewModel(
    val goalId: Int,
    val repository: GoalRepository,
    public val navigator: Navigator,
): ViewModel() {
    private val _usState = MutableStateFlow<GoalDetailUIState>(GoalDetailUIState.Idle())
    val uiState = _usState.asStateFlow()

    init {
        viewModelScope.launch {
            val goalWithParameters = withContext(Dispatchers.IO) {
                repository.getByIdWithParameters(goalId)
            }

            if (goalWithParameters == null) {
                _usState.update {
                    GoalDetailUIState.Failed()
                }
                navigator.goBack()
                return@launch
            } else {
                _usState.update { GoalDetailUIState.Success(goalWithParameters.goal) }
            }
        }
    }
}