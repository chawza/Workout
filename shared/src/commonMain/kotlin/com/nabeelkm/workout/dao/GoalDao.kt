package com.nabeelkm.workout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Upsert
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Parameter
import kotlinx.coroutines.flow.Flow


data class GoalWithParameter(
    @Embedded val goal: Goal,
    @Relation(parentColumn = "id", entityColumn = "goalId")
    val parameters: List<Parameter>
)
@Dao
interface GoalDao {
    @Insert
    suspend fun insert(goal: Goal)

    @Delete
    suspend fun delete(goal: Goal)

    @Query("SELECT * FROM Goal WHERE status = :status ORDER BY createdAt DESC")
    fun getAll(status: Int): Flow<List<Goal>>
    @Query("SELECT * FROM Goal ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Goal>>

    @Query("SELECT * FROM Goal WHERE id = :id")
    suspend fun getById(id: Int): Goal?

    @Upsert
    suspend fun upsert(goal: Goal)

    @Transaction
    @Query("SELECT * FROM Goal WHERE id = :id")
    fun getByIdWithParameters(id: Int): Flow<GoalWithParameter?>
}