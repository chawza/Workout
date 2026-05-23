package com.nabeelkm.workout.database

import androidx.room.RoomDatabase


expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>