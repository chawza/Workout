package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.viewmodel.FormState
import com.nabeelkm.workout.viewmodel.WorkoutFormState
import com.nabeelkm.workout.viewmodel.WorkoutFormViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock


@Composable
fun WorkoutFormScreen(
    viewModel: WorkoutFormViewModel = koinViewModel()
) {

    val formState = viewModel.formStateFlow.collectAsState()

    WorkoutFormContent(
        formState.value,
        onAddWorkout = viewModel::onAddForm,
        onCancel = {},
        selectableGoals = listOf(),
    )

}

@Composable
fun WorkoutFormContent(
    formState: WorkoutFormState,
    onAddWorkout: () -> Unit,
    onCancel: () -> Unit,
    selectableGoals: List<Goal> = listOf(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Log Workout",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(18.dp, 10.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpace.s4)
        ) {
            // Start Date
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpace.s4),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Goal",
                    style = MaterialTheme.typography.bodySmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(AppSpace.s2, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy(AppSpace.s2)
                ) {
                    selectableGoals.forEach { goal ->
                        GoalPill(goal, onClick = {}, isSelected = false)
                    }
                }
            }

            // Start Date
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpace.s4),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Start Time",
                    style = MaterialTheme.typography.bodySmall
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(AppSpace.s4)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1F),
                            value = formState.time.toString(),
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            placeholder = {
                                Text("Select a date")
                            }
                        )

                        OutlinedTextField(
                            modifier = Modifier.weight(1F),
                            value = formState.time.toString(),
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            placeholder = {
                                Text("Select a date")
                            }
                        )
                    }
                }
            }

            // Start Date
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = AppSpace.s4),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Duration (m)",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.duration?.let { (it / 1_000_000).toString() } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    placeholder = {
                        Text("30")
                    },
                )
            }
        }

    }

}

@Composable
fun GoalPill(goal: Goal, onClick: () -> Unit, isSelected: Boolean) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = AppSpace.s3, vertical = AppSpace.s2),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (!isSelected) AppColor.surface else AppColor.accent,
            contentColor = AppColor.fg2
        ),
        border = BorderStroke(1.dp, color = if (isSelected) AppColor.accent else AppColor.border)
    ) {
        Box(
            modifier = Modifier.size(16.dp),
            contentAlignment = Alignment.Center
        ) {

        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = goal.name,
            style = MaterialTheme.typography.bodySmall,
            color = AppColor.fg2
        )
    }
}

@Preview
@Composable
fun WorkoutFormPreview() {
    Theme {
        WorkoutFormContent(
            onAddWorkout = {},
            onCancel = {},
            formState = WorkoutFormState(),
            selectableGoals = listOf(
                Goal(
                    name = "Half Marathon Prep",
                    id = 0,
                    status = GoalStatus.ACTIVE.value,
                    notes = "",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    completedAt = null,
                    startAt = null
                ),
                Goal(
                    name = "Daily 5K",
                    id = 0,
                    status = GoalStatus.ACTIVE.value,
                    notes = "",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    completedAt = null,
                    startAt = null
                ),
                Goal(
                    name = "Upper Body Strength",
                    id = 0,
                    status = GoalStatus.ACTIVE.value,
                    notes = "",
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    completedAt = null,
                    startAt = null
                )
            )
        )
    }
}
