package com.nabeelkm.workout.repository

import com.nabeelkm.workout.dao.GoalWithParameter
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import kotlinx.coroutines.flow.Flow


class GoalRepository(
    private val database: AppDatabase
) {
    private val dao = database.goalDao()
    val activeGoals = dao.getAll(status = GoalStatus.ACTIVE.value)
    val newGoals = dao.getAll(status = GoalStatus.NEW.value)
    val completedGoals = dao.getAll(status = GoalStatus.COMPLETED.value)

    suspend fun insertOne(goal: Goal) {
        dao.insert(goal)
    }

    suspend fun delete(goal: Goal) {
        dao.delete(goal)
    }

    suspend fun getById(id: Int): Goal? {
        return dao.getById(id)
    }

    fun getByIdWithParametersFlow(id: Int): Flow<GoalWithParameter?> {
        return dao.getByIdWithParameters(id)
    }

    suspend fun upsert(goal: Goal) {
        dao.upsert(goal)
    }
    suspend fun updateStatus(goal: Goal, state: GoalStatus) {
        dao.upsert(
            goal.copy(status = state.value)
        )
    }
}