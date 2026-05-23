package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalStatus(
    val value: Int
) {
    NEW(1),
    ACTIVE(2),
    COMPLETED(3),
}
@Entity
data class Goal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val status: Int,
    val createdAt: Int,
    val completedAt: Int?,
    val startAt: Int?
) {
    fun getStatus(): GoalStatus {
        return GoalStatus.entries[status]
    }
}