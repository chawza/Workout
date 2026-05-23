package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.nabeelkm.workout.entity.Parameter
import kotlinx.coroutines.flow.Flow

@Dao
interface ParameterDao {
    @Insert
    suspend fun insert(goal: Parameter)

    @Query("SELECT * FROM Parameter")
    fun getAll(): Flow<List<Parameter>>
}
