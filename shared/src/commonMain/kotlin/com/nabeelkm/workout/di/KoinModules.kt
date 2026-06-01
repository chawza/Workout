package com.nabeelkm.workout.di

import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.database.getDatabaseBuilder
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.repository.WorkoutRepository
import com.nabeelkm.workout.viewmodel.GoalDetailViewModel
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import com.nabeelkm.workout.viewmodel.WorkoutFormViewModel
import com.nabeelkm.workout.viewmodel.WorkoutIndexViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }
    singleOf(::GoalRepository)
    singleOf(::WorkoutRepository)
    viewModelOf(::GoalIndexViewModel)
    viewModelOf(::GoalDetailViewModel)
    viewModelOf(::GoalFormViewModel)
    viewModelOf(::WorkoutFormViewModel)
    viewModelOf(::WorkoutIndexViewModel)
}

expect val platformModule: Module
