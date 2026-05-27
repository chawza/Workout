package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoalIndexViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {
    val activeGoals: StateFlow<List<Goal>> = goalRepository.activeGoals.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val newGoals: StateFlow<List<Goal>> = goalRepository.newGoals.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun deleteGoal(goal: Goal) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            goalRepository.delete(goal)
        }
    }
}