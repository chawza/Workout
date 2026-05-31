package com.nabeelkm.workout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.BottomNavigation
import com.nabeelkm.workout.ui.screens.GoalDetailScreen
import com.nabeelkm.workout.ui.screens.GoalFormScreen
import com.nabeelkm.workout.ui.screens.GoalIndexScreen
import com.nabeelkm.workout.viewmodel.GoalDetailViewModel
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
sealed class Screen {
    data object GoalIndex : Screen()
    class GoalIndexDetail(val goalId: Int): Screen()
    data object GoalAddForm: Screen()
    class GoalEditForm(val goalId: Int): Screen()
    data object Home : Screen()
    data object Settings: Screen()
}

@Composable
fun App() {
    val backStack = remember { mutableStateListOf<Screen>(Screen.GoalIndex) }
    val navigator = remember { Navigator(backStack) }
    val repository = koinInject<GoalRepository>()
    val routes = remember {
        entryProvider<Screen> {
            entry<Screen.GoalIndex> {
                GoalIndexScreen(
                    viewModel = viewModel<GoalIndexViewModel> { GoalIndexViewModel(repository) },
                    navigator = navigator
                )
            }
            entry<Screen.GoalIndexDetail> { screen ->
                val vm = viewModel<GoalDetailViewModel> { GoalDetailViewModel(repository, navigator) }
                LaunchedEffect(screen.goalId) {
                    vm.loadGoal(screen.goalId)
                }
                GoalDetailScreen(viewModel = vm)
            }
            entry<Screen.GoalAddForm> {
                GoalFormScreen(
                    viewModel = viewModel<GoalFormViewModel> { GoalFormViewModel(repository, navigator) },
                    navigator = navigator
                )
            }
            entry<Screen.GoalEditForm> { screen ->
                val vm = viewModel<GoalFormViewModel> { GoalFormViewModel(repository, navigator) }
                LaunchedEffect(screen.goalId) {
                    vm.loadGoal(screen.goalId)
                }
                GoalFormScreen(viewModel = vm, navigator = navigator)
            }
            entry<Screen.Home> {
            }
        }
    }
    Theme {
        Scaffold(
            bottomBar = {
                BottomNavigation(navigator)
            }
        ) { innerPadding ->
            NavDisplay(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                backStack = backStack,
                onBack = { navigator.goBack() },
                entryProvider = routes
            )
        }
    }
}
