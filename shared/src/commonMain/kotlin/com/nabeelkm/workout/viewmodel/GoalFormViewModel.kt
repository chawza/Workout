package com.nabeelkm.workout.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.repository.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class FormState(
    val goalName: String = "",
    val parameters: List<Parameter> = listOf(),
    val startAt: Long? = null
)
class GoalFormViewModel(
    val goalRepository: GoalRepository
): ViewModel() {
    private val _formState = MutableStateFlow(FormState())
    val formState = _formState.asStateFlow()
}