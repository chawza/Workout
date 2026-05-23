package com.nabeelkm.workout.di

import com.nabeelkm.workout.Screen
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.database.getDatabaseBuilder
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.ui.screens.GoalIndexScreen
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val sharedModule = module {
    single<AppDatabase> {
        getDatabaseBuilder().build()
    }
    singleOf(::GoalRepository)
    viewModelOf(::GoalIndexViewModel)

    navigation<Screen.GoalIndex> {
        GoalIndexScreen(
            viewModel = koinViewModel()
        )
    }
    navigation<Screen.Home> {

    }
}

expect val platformModule: Module