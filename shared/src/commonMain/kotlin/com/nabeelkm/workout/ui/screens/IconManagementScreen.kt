package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppRadius
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.GOAL_ICONS
import com.nabeelkm.workout.ui.components.GoalIconDef
import com.nabeelkm.workout.ui.components.WorkoutIcons
import com.nabeelkm.workout.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun IconManagementScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navigator: AppNavigator,
) {
    val enabledIcons by viewModel.enabledIcons.collectAsStateWithLifecycle()

    IconManagementContent(
        enabledIcons = enabledIcons,
        onToggleIcon = viewModel::toggleIcon,
        onBack = { navigator.goBack() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IconManagementContent(
    enabledIcons: Set<String>,
    onToggleIcon: (String) -> Unit = {},
    onBack: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = onBack,
                            contentPadding = PaddingValues(start = 0.dp, end = AppSpace.s2, top = 8.dp, bottom = 8.dp),
                        ) {
                            Icon(
                                imageVector = WorkoutIcons.Back,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(18.dp),
                            )
                            Spacer(Modifier.size(AppSpace.s1))
                            Text("Settings", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.size(AppSpace.s2))
                        Text("Workout Icons", style = MaterialTheme.typography.titleMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(AppSpace.s4),
            verticalArrangement = Arrangement.spacedBy(AppSpace.s4),
        ) {
            Text(
                "Choose which icons are available when creating a goal. " +
                    "Disabling an icon won’t affect goals already using it.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(verticalArrangement = Arrangement.spacedBy(AppSpace.s3)) {
                GOAL_ICONS.chunked(2).forEach { rowIcons ->
                    Row(horizontalArrangement = Arrangement.spacedBy(AppSpace.s3)) {
                        rowIcons.forEach { def ->
                            IconToggleCard(
                                def = def,
                                enabled = def.id in enabledIcons,
                                onClick = { onToggleIcon(def.id) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                        // Keep a tidy 2-column grid if the row is short.
                        repeat(2 - rowIcons.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun IconToggleCard(
    def: GoalIconDef,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.55f)
            .clip(RoundedCornerShape(AppRadius.md))
            .background(if (enabled) AppColor.accentSoft else MaterialTheme.colorScheme.surface)
            .border(
                width = 1.5.dp,
                color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(AppRadius.md),
            )
            .clickable { onClick() }
            .padding(horizontal = AppSpace.s4, vertical = AppSpace.s3),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppSpace.s3),
    ) {
        // White rounded-square icon container.
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(AppRadius.md))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(AppRadius.md)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = def.image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }

        Text(
            text = def.displayName,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )

        // Checkbox.
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(AppRadius.sm))
                .background(if (enabled) MaterialTheme.colorScheme.primary else Color.Transparent)
                .border(
                    width = 1.5.dp,
                    color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(AppRadius.sm),
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (enabled) {
                Icon(
                    imageVector = WorkoutIcons.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconManagementPreview() {
    Theme {
        IconManagementContent(
            enabledIcons = setOf("run", "walk", "strength", "bike", "swim", "yoga"),
        )
    }
}
