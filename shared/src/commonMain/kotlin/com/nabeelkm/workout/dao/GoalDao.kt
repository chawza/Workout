package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Upsert
import com.nabeelkm.workout.entity.Goal
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * FROM Goal WHERE status = :status ORDER BY createdAt DESC")
    fun getAll(status: Int): Flow<List<Goal>>

    @Query("SELECT * FROM Goal WHERE id = :id")
    suspend fun getById(id: Int): Goal?

    @Upsert
    suspend fun upsert(goal: Goal)
}