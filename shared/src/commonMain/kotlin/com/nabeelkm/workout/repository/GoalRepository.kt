package com.nabeelkm.workout.repository

import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.entity.Goal
import kotlinx.coroutines.flow.Flow


class GoalRepository(
    private val database: AppDatabase
) {
    private val dao = database.goalDao()
    val goals = dao.getAll()
}