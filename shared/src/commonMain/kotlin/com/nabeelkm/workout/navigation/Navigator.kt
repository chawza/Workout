package com.nabeelkm.workout.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nabeelkm.workout.Screen

class Navigator(
    private val backStack: SnapshotStateList<Screen>
) {
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