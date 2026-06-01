package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkm.workout.entity.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao  {
    @Insert
    suspend fun insert(workout: Workout): Long

    @Query("SELECT * FROM Workout WHERE id = :id")
    fun getByID(id: Long): Workout?

    @Query("SELECT * FROM Workout ORDER BY time")
    fun getAll(): Flow<List<Workout>>

    @Query("SELECT * FROM Workout WHERE goalId = :goalId ORDER BY time")
    fun getAllAsFlow(goalId: Int): Flow<List<Workout>>
}
