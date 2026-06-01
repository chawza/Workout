package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class WorkoutIndexViewModel(
    val workoutRepository: WorkoutRepository
): ViewModel() {
    val recentWorkoutFlow: Flow<List<Workout>> = workoutRepository.recentWorkoutFlow()
}