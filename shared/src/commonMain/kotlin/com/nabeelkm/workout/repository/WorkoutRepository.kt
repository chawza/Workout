package com.nabeelkm.workout.repository

import com.nabeelkm.workout.dao.GoalWithParameter
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Workout
import kotlinx.coroutines.flow.Flow

class WorkoutRepository (
    private val database: AppDatabase
) {
    private val dao = database.workoutDao()
    private val goalDao = database.goalDao()

    suspend fun insertOne(workout: Workout): Long {
        return dao.insert(workout)
    }

    fun getById(id: Int): Workout? {
        return dao.getByID(id)
    }

    fun recentWorkoutFlow(): Flow<List<Workout>> {
        return dao.getAll()
    }

    fun getGoalWorkoutsFlow(id: Int): Flow<List<Workout>> {
        return dao.getAllAsFlow(id)
    }
}
