package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Goal::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["time"]),
        Index(value = ["goalId"])
    ]
)
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val goalId: Int,
    val time: Long,
    val duration: Long?,
    val notes: String? = null
)