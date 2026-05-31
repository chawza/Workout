package com.nabeelkm.workout.di

import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.database.getDatabaseBuilder
import com.nabeelkm.workout.repository.GoalRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }
    singleOf(::GoalRepository)
}

expect val platformModule: Module
