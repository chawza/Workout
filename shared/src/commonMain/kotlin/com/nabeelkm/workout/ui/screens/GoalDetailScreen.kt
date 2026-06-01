package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.ui.components.Card
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.utils.formatDate
import com.nabeelkm.workout.utils.formatDateTime
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.AppRoute
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.viewmodel.GoalDetailUIState
import com.nabeelkm.workout.viewmodel.GoalDetailViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.workout_icon
import kotlin.time.Instant


@Composable
fun GoalDetailScreen(
    goalId: Int,
    viewModel: GoalDetailViewModel = koinViewModel(),
    navigator: AppNavigator,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val workoutsState = viewModel.workoutsStateFLow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(goalId) {
        viewModel.loadGoal(goalId)
    }

    when (val state = uiState.value) {
        is GoalDetailUIState.Idle -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
        is GoalDetailUIState.Failed -> {
            Box(contentAlignment = Alignment.Center) {
                Text("Invalid State :(")
            }
        }
        is GoalDetailUIState.Success -> {
            GoalDetailContent(
                goal = state.goalWithParameter.goal,
                parameters = state.goalWithParameter.parameters,
                workouts = workoutsState.value,
                onAddWorkout = {
                    navigator.navigate(
                        AppRoute.LogWorkout(goalId=goalId)
                    )
                },
                onSwitchState = { status ->
                    scope.launch {
                        viewModel.switchState(status)
                    }
                },
                navigateToEditScreen = {
                    navigator.navigate(AppRoute.UpdateGoal(state.goalWithParameter.goal.id))
                },
            )
        }
    }

}

@Composable
private fun GoalDetailContent(
    goal: Goal,
    parameters: List<Parameter> = listOf(),
    workouts: List<Workout> = listOf(),
    onAddWorkout: () -> Unit = {},
    onSwitchState: (GoalStatus) -> Unit = {},
    navigateToEditScreen: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Goals")
                },
                actions = {
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            navigateToEditScreen()
                        },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(18.dp, 10.dp)
                    ) {
                        Text("Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            GoalDetailHeader(goal, onSwitchState)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Tracked Parameters".uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )
                GoalParameterList(parameters)
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAddWorkout,
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text("+ Add Workout")
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Recent Workout".uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    workouts.forEach { workout ->
                        WorkoutDetailCard(workout = workout, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
fun GoalDetailHeader(
    goal: Goal,
    onSwitchState: (GoalStatus) -> Unit
) {
    val goalStatus = GoalStatus.fromValue(goal.status)

    Card (
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MultiChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttonColors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.background,
                )
                SegmentedButton(
                    checked = goalStatus == GoalStatus.NEW,
                    onCheckedChange = {
                        onSwitchState(GoalStatus.NEW)
                    },
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 0.dp, bottomStart = 6.dp, bottomEnd = 0.dp),
                    colors = buttonColors,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    icon = {}
                ) {
                    Text("New")
                }
                SegmentedButton(
                    checked = goalStatus == GoalStatus.ACTIVE,
                    onCheckedChange = {
                        onSwitchState(GoalStatus.ACTIVE)
                    },
                    shape = RoundedCornerShape(0.dp),
                    colors = buttonColors,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    icon = {}
                ) {
                    Text("Active")
                }
                SegmentedButton(
                    checked = goalStatus == GoalStatus.COMPLETED,
                    onCheckedChange = {
                        onSwitchState(GoalStatus.COMPLETED)
                    },
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 6.dp, bottomStart = 0.dp, bottomEnd = 6.dp),
                    colors = buttonColors,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    icon = {}
                ) {
                    Text("Completed")
                }
            }

            Text(
                goal.name.ifEmpty { "My Goal" },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "Goal #${goal.id}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    goal.startAt?.let { formatDateTime(it) } ?: "-"
                )
                Text("-", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant) )
                Text(
                    goal.completedAt?.let { formatDateTime(it) } ?: "-"
                )
            }

            Text(
                "Created ${formatDate(goal.createdAt)}",
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GoalParameterList(parameters: List<Parameter> = listOf()) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        parameters.forEach { parameter ->
            Card(
                modifier = Modifier.weight(1F),
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        parameter.name,
                        style = MaterialTheme.typography.bodySmallEmphasized,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        parameter.unit ?: "Unit",
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        ParameterType.fromValue(parameter.type).toString().uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutDetailCard(modifier: Modifier = Modifier, workout: Workout) {
    Card {
        Row(
            modifier = modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(Res.drawable.workout_icon),
                        contentDescription = "Workout Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val hasNotes = workout.notes?.length?.let { it > 0 } ?: false
                    Text(
                        text = if (hasNotes) {
                            workout.notes
                        } else {
                            "no notes"
                        },
                        maxLines = 1,
                        fontStyle = if (!hasNotes) FontStyle.Italic else null,
                        color = if (!hasNotes) AppColor.muted else AppColor.fg,
                    )
                    Text(
                        text = "5.2km - 32min"
                    )
                }
                Text("Today")
            }
        }
    }
}

@Preview
@Composable
private fun GoalDetailPreview() {
    Theme {
        GoalDetailContent(
            goal = Goal(
                1,
                "Rucking",
                status = GoalStatus.NEW.value,
                createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                startAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                completedAt  = Instant.parse("2024-06-24T10:30:00Z").toEpochMilliseconds(),
            ),
            parameters = listOf(
                Parameter(
                    id = 1,
                    goalId = 1,
                    name = "Distance",
                    unit = "KM",
                    type = ParameterType.FLOAT.value
                ),
                Parameter(
                    id = 2,
                    goalId = 1,
                    name = "Duration",
                    unit = "min",
                    type = ParameterType.INTEGER.value
                ),
                Parameter(
                    id = 3,
                    goalId = 1,
                    name = "pace",
                    unit = "min/km",
                    type = ParameterType.FLOAT.value
                ),
            ),
            workouts = listOf(
                Workout(
                    id = 1,
                    goalId = 1,
                    notes = "Morning Run",
                    time = Instant.parse("2024-07-24T10:30:00Z").toEpochMilliseconds(),
                    duration = null
                ),
                Workout(
                    id = 2,
                    goalId = 1,
                    notes = "Evening Run",
                    time = Instant.parse("2024-08-24T10:30:00Z").toEpochMilliseconds(),
                    duration = null
                )
            )
        )
    }
}