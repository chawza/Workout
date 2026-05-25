package com.nabeelkm.workout.navigation

import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nabeelkm.workout.Screen
import kotlinx.coroutines.flow.map

class Navigator(
    private val backStack: SnapshotStateList<Screen>
) {
    val currentScreenFlow = snapshotFlow { backStack.toList() }.map { it.lastOrNull() }

    fun navigate(screen: Screen) {
        backStack.add(screen)
    }

    fun goBack() {
        backStack.removeLastOrNull()
    }

    fun replace(screen: Screen) {
        backStack.removeLastOrNull()
        backStack.add(screen)
    }

    fun setRoot(screen: Screen) {
        backStack.clear()
        backStack.add(screen)
    }
}