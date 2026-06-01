package com.nabeelkm.workout

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.AppRoute
import com.nabeelkm.workout.navigation.HomeNavigator
import com.nabeelkm.workout.navigation.HomeRoute
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.BottomNavigation
import com.nabeelkm.workout.ui.screens.GoalDetailScreen
import com.nabeelkm.workout.ui.screens.GoalFormScreen
import com.nabeelkm.workout.ui.screens.GoalIndexScreen
import com.nabeelkm.workout.ui.screens.SettingsScreen
import com.nabeelkm.workout.ui.screens.WorkoutDetailScreen
import com.nabeelkm.workout.ui.screens.WorkoutFormScreen
import com.nabeelkm.workout.ui.screens.WorkoutIndexScreen

@Composable
fun App() {
    val appBackStack = remember { mutableStateListOf<AppRoute>(AppRoute.Home(HomeRoute.GoalIndex)) }
    val appNavigator = remember { AppNavigator(appBackStack) }

    val routes = remember {
        entryProvider {
            entry<AppRoute.Home> {
                val homeBackStack = remember { mutableStateListOf<HomeRoute>(HomeRoute.GoalIndex) }
                val homeNavigator = remember { HomeNavigator(homeBackStack, appNavigator) }

                Scaffold(
                    bottomBar = {
                        BottomNavigation(homeNavigator)
                    }
                ) { innerPadding ->
                    val homeRoutes = remember {
                        entryProvider {
                            entry<HomeRoute.GoalIndex> {
                                GoalIndexScreen(
                                    navigator = homeNavigator
                                )
                            }
                            entry<HomeRoute.Home> {
                                WorkoutIndexScreen(
                                    navigator = homeNavigator
                                )
                            }
                            entry<HomeRoute.Settings> {
                                SettingsScreen()
                            }
                        }
                    }

                    NavDisplay(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        backStack = homeBackStack,
                        onBack = { homeNavigator.goBack() },
                        entryProvider = homeRoutes
                    )
                }
            }
            entry<AppRoute.AddGoal> {
                GoalFormScreen(
                    navigator = appNavigator
                )
            }
            entry<AppRoute.UpdateGoal> { route ->
                GoalFormScreen(
                    goalId = route.goalId,
                    navigator = appNavigator
                )
            }
            entry<AppRoute.DetailGoal> { route ->
                GoalDetailScreen(
                    goalId = route.goalId,
                    navigator = appNavigator
                )
            }
            entry<AppRoute.LogWorkout> {
                WorkoutFormScreen(
                    navigator = appNavigator
                )
            }
            entry<AppRoute.WorkoutDetail> { route ->
                WorkoutDetailScreen(
                    workoutId = route.workoutId,
                    navigator = appNavigator
                )
            }
        }
    }
    Theme {
        NavDisplay(
            modifier = Modifier.fillMaxSize(),
            backStack = appBackStack,
            onBack = { appNavigator.goBack() },
            entryProvider = routes
        )
    }
}
