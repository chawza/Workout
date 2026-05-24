package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkm.workout.entity.WorkoutParameter
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutParameterDao   {
    @Insert
    suspend fun insert(workoutParameter: WorkoutParameter)

    @Query("SELECT * FROM WorkoutParameter")
    fun getAll(): Flow<List<WorkoutParameter>>
}
