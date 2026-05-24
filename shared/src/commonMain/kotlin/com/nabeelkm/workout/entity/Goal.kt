package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.koin.core.logger.Logger

enum class GoalStatus(
    val value: Int,
    val label: String
) {
    NEW(1, "New"),
    ACTIVE(2, "Active"),
    COMPLETED(3, "Completed");

    companion object {
        fun fromValue(value: Int): GoalStatus {
            return entries.find { it.value == value } ?: NEW
        }
    }
}
@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val status: Int,
    val createdAt: Long,
    val completedAt: Long?,
    val startAt: Long?
) {
    /**
     * Returns the GoalStatus corresponding to the status value.
     * Fixed: Used fromValue instead of entries index to avoid IndexOutOfBoundsException
     * as status values are 1-indexed.
     */
    fun getStatus(): GoalStatus {
        return GoalStatus.fromValue(status)
    }
}