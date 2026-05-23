package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class GoalStatus(
    val value: Int,
    val label: String
) {
    NEW(1, "New"),
    ACTIVE(2, "Active"),
    COMPLETED(3, "Completed"),
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