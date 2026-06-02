package com.nabeelkm.workout.viewmodel

import androidx.lifecycle.ViewModel
import com.nabeelkm.workout.ui.components.GOAL_ICONS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory store for the set of goal icons the user has made available
 * in the goal form's picker.
 *
 * This lives entirely in the UI/ViewModel layer for now — it is shared
 * (as a process-wide singleton) between [SettingsViewModel] (the
 * management screen) and [GoalFormViewModel] (the picker) so toggling an
 * icon is immediately reflected in the form. It is *not* yet persisted to
 * the database; restarting the app resets to "all enabled".
 */
object IconPreferences {
    private val _enabledIcons = MutableStateFlow(GOAL_ICONS.map { it.id }.toSet())
    val enabledIcons: StateFlow<Set<String>> = _enabledIcons.asStateFlow()

    /**
     * Toggle an icon on/off. At least one icon must always remain enabled,
     * so attempting to disable the last one is a no-op.
     */
    fun toggle(id: String) {
        _enabledIcons.update { current ->
            when {
                id !in current -> current + id
                current.size <= 1 -> current
                else -> current - id
            }
        }
    }
}

class SettingsViewModel : ViewModel() {
    val enabledIcons: StateFlow<Set<String>> = IconPreferences.enabledIcons

    fun toggleIcon(id: String) = IconPreferences.toggle(id)
}
