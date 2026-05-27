package com.nabeelkm.workout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.BottomNavigation
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf

@Serializable
sealed class Screen {
    data object GoalIndex : Screen()
    data object GoalAddForm: Screen()
    class GoalEditForm(val goalId: Int): Screen()
    data object Home : Screen()
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    val backStack = remember { mutableStateListOf<Screen>(Screen.GoalIndex) }
    val navigator = koinInject<Navigator> { parametersOf(backStack) }
    val entryProvider = koinEntryProvider<Any>()
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
                entryProvider = entryProvider
            )
        }
    }
}