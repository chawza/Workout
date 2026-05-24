package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkm.workout.entity.Workout
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao  {
    @Insert
    suspend fun insert(workout: Workout)

    @Query("SELECT * FROM Workout")
    fun getAll(): Flow<List<Workout>>
}
