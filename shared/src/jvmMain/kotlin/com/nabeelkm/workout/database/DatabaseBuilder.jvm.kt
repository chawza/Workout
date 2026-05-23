package com.nabeelkm.workout.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("user.home"), "workout.db")
    return Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
}
