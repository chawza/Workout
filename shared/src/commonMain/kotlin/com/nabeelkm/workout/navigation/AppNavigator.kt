package com.nabeelkm.workout.navigation

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    class Home(val route: HomeRoute): AppRoute()
    data object AddGoal: AppRoute()
    class UpdateGoal(val goalId: Int): AppRoute()
    class DetailGoal(val goalId: Int): AppRoute()
    class LogWorkout(val goalId: Int? = null): AppRoute()
    class WorkoutDetail(val workoutId: Long): AppRoute()
}

class AppNavigator(
    private val backStack: SnapshotStateList<AppRoute>
) {
    val currentScreenFlow = snapshotFlow { backStack.toList() }.map { it.lastOrNull() }

    fun navigate(route: AppRoute) {
        backStack.add(route)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun replace(route: AppRoute) {
        backStack.removeLastOrNull()
        backStack.add(route)
    }

    fun setRoot(route: AppRoute) {
        backStack.clear()
        backStack.add(route)
    }
}