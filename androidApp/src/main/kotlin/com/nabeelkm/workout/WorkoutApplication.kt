package com.nabeelkm.workout

import android.app.Application
import com.nabeelkm.workout.database.appContext
import com.nabeelkm.workout.di.initKoin
import org.koin.android.ext.koin.androidContext

class WorkoutApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initKoin {
            androidContext(applicationContext)
        }
    }
}
