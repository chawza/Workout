package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkm.workout.entity.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal)

    @Query("SELECT * FROM Goal")
    fun getAll(): Flow<List<Goal>>
}