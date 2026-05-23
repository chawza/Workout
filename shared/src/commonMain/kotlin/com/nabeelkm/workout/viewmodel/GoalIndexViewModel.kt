package com.nabeelkm.workout.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.rememberViewModelStoreProvider
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GoalIndexViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {
    val goalsState: StateFlow<List<Goal>> = goalRepository.goals.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}