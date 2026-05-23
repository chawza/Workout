package com.nabeelkm.workout.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nabeelkm.workout.dao.GoalDao
import com.nabeelkm.workout.dao.ParameterDao
import com.nabeelkm.workout.dao.WorkoutDao
import com.nabeelkm.workout.dao.WorkoutParameterDao
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.entity.WorkoutParameter

@Database(
    entities = [
        Goal::class,
        Parameter::class,
        Workout::class,
        WorkoutParameter::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun goalDao(): GoalDao
    abstract fun parameterDao(): ParameterDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutParameter(): WorkoutParameterDao
}