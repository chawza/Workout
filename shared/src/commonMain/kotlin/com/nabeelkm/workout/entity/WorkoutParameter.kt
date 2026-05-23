package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Parameter::class,
            parentColumns = ["id"],
            childColumns = ["parameter_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["parameter_id"]),
        Index(value = ["workout_id", "parameter_id"]),
    ]
)
data class WorkoutParameter(
    @PrimaryKey val id: Int,
    val workoutId: Int,
    val parameterId: Int,
    val valueFloat: Float?,
    val valueInteger: Int?,
    val valueString: String?,
)
