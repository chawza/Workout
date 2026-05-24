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
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Parameter::class,
            parentColumns = ["id"],
            childColumns = ["parameterId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["parameterId"]),
        Index(value = ["workoutId", "parameterId"]),
    ]
)
data class WorkoutParameter(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val workoutId: Int,
    val parameterId: Int,
    val valueFloat: Float?,
    val valueInteger: Int?,
    val valueString: String?,
)
