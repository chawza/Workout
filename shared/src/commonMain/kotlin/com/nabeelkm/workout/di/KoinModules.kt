package com.nabeelkm.workout.di

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nabeelkm.workout.Screen
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.database.getDatabaseBuilder
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.ui.screens.GoalDetailScreen
import com.nabeelkm.workout.ui.screens.GoalFormScreen
import com.nabeelkm.workout.ui.screens.GoalIndexScreen
import com.nabeelkm.workout.viewmodel.GoalDetailViewModel
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
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
    viewModelOf(::GoalFormViewModel)
    viewModelOf(::GoalDetailViewModel)

    single { (backStack: SnapshotStateList<Screen>) ->
        Navigator(backStack)
    }

    navigation<Screen.GoalIndex> {
        GoalIndexScreen(
            viewModel = koinViewModel(),
            navigator = get()
        )
    }
    navigation<Screen.GoalIndexDetail> {
        val viewModel = koinViewModel<GoalDetailViewModel>()
        LaunchedEffect(it.goalId) {
            viewModel.loadGoal(it.goalId)
        }
        GoalDetailScreen(
            viewModel = viewModel,
        )
    }
    navigation<Screen.GoalAddForm> {
        GoalFormScreen(
            viewModel = koinViewModel(),
            navigator = get()
        )
    }
    navigation<Screen.GoalEditForm> {
        val vm = koinViewModel<GoalFormViewModel>()
        LaunchedEffect(it.goalId) {
            vm.loadGoal(it.goalId)
        }
        GoalFormScreen(
            viewModel = vm,
            navigator = get(),
        )
    }
    navigation<Screen.Home> {

    }
}

expect val platformModule: Module