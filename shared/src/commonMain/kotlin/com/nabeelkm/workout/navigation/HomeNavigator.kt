package com.nabeelkm.workout.navigation

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
sealed class HomeRoute {
    data object GoalIndex: HomeRoute()
    data object Home: HomeRoute()
    data object Settings: HomeRoute()
}


class HomeNavigator(
    private val backStack: SnapshotStateList<HomeRoute>,
    private val appNavigator: AppNavigator,
) {
    val currentScreenFlow = snapshotFlow { backStack.toList() }.map { it.lastOrNull() }

    fun navigate(route: HomeRoute) {
        backStack.add(route)
    }
    fun navigate(route: AppRoute) {
        appNavigator.navigate(route)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun replace(route: HomeRoute) {
        backStack.removeLastOrNull()
        backStack.add(route)
    }

    fun setRoot(route: HomeRoute) {
        backStack.clear()
        backStack.add(route)
    }
}

