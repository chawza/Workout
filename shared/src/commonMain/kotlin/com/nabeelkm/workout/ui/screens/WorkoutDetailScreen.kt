package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.entity.WorkoutParameter
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.AppRoute
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppRadius
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.AppText
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.Card
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.utils.formatDateTime
import org.jetbrains.compose.resources.painterResource
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.workout_icon
import kotlin.time.Clock

@Composable
fun WorkoutDetailScreen(
    workoutId: Long,
    navigator: AppNavigator,
) {
    // Placeholder — data loading will be wired via a ViewModel later
    WorkoutDetailContent(
        workout = null,
        goal = null,
        parameters = emptyList(),
        parameterValues = emptyList(),
        onBack = { navigator.goBack() },
        onDelete = {},
        onEdit = {
            navigator.navigate(AppRoute.UpdateLogWorkout(workoutId))
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WorkoutDetailContent(
    workout: Workout?,
    goal: Goal?,
    parameters: List<Parameter>,
    parameterValues: List<WorkoutParameter>,
    onBack: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = AppColor.fg2
                        ),
                        onClick = onBack,
                        shape = RoundedCornerShape(AppRadius.md),
                        contentPadding = PaddingValues(12.dp, 8.dp)
                    ) {
                        Text("← Back", style = MaterialTheme.typography.bodyMedium)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = onEdit,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(18.dp, 10.dp)
                    ) {
                        Text("Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.bg
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppColor.border,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .background(AppColor.bg)
                    .padding(AppSpace.s4),
                horizontalArrangement = Arrangement.spacedBy(AppSpace.s3)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onDelete,
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = AppColor.danger
                    ),
                    contentPadding = PaddingValues(18.dp, 10.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(Res.drawable.workout_icon),
                        contentDescription = null,
                        tint = AppColor.danger
                    )
                    Text(
                        " Delete",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = onBack,
                    contentPadding = PaddingValues(18.dp, 10.dp)
                ) {
                    Text("Done")
                }
            }
        }
    ) { innerPadding ->
        if (workout == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Loading...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColor.muted
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpace.s4),
            verticalArrangement = Arrangement.spacedBy(AppSpace.s4)
        ) {
            // Workout title
            Text(
                text = workout.notes?.takeIf { it.isNotBlank() } ?: "Workout",
                style = MaterialTheme.typography.displayLarge.copy(
                    letterSpacing = (-0.018).sp
                )
            )

            // Goal info row
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpace.s3),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(AppRadius.md))
                        .background(
                            if (goal != null) AppColor.accentSoft
                            else AppColor.surfaceWarm
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(Res.drawable.workout_icon),
                        contentDescription = null,
                        tint = AppColor.accent
                    )
                }
                Column {
                    Text(
                        goal?.name ?: "—",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        "${formatDateTime(workout.time)} ",
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColor.muted
                    )
                }
            }

            // Stats grid
            val statItems = buildList {
                add(StatItem("${workout.duration ?: 0}m", "Duration"))
                for (param in parameters) {
                    val wp = parameterValues.find { it.parameterId == param.id }
                    if (wp != null) {
                        val displayValue = wp.valueFloat?.let {
                            if (it == it.toLong().toFloat()) it.toLong().toString()
                            else it.toString()
                        } ?: wp.valueInteger?.toString()
                        ?: wp.valueString
                        ?: continue
                        val label = if (param.unit != null) "${param.name} (${param.unit})"
                        else param.name
                        add(StatItem(displayValue, label))
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(AppSpace.s3),
                verticalArrangement = Arrangement.spacedBy(AppSpace.s3),
            ) {
                statItems.forEach { stat ->
                    StatBox(
                        modifier = Modifier.weight(1f),
                        value = stat.value,
                        label = stat.label
                    )
                }
            }

            // Notes
            if (!workout.notes.isNullOrBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(AppSpace.s1)) {
                    Text(
                        "NOTES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = AppColor.muted,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.04.sp,
                        )
                    )
                    Text(
                        workout.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColor.fg2
                    )
                }
            }
        }
    }
}

private data class StatItem(val value: String, val label: String)

@Composable
private fun StatBox(
    modifier: Modifier = Modifier,
    value: String,
    label: String
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(AppSpace.s3)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = value,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = AppText.xl,
                color = AppColor.fg,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.02).sp,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                fontSize = AppText.xs,
                color = AppColor.muted,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun WorkoutDetailPreview() {
    val now = Clock.System.now().toEpochMilliseconds()

    Theme {
        WorkoutDetailContent(
            workout = Workout(
                id = 112,
                goalId = 1,
                time = now,
                duration = 72,
                notes = "Long Run"
            ),
            goal = Goal(
                id = 1,
                name = "Half Marathon Prep",
                status = GoalStatus.ACTIVE.value,
                createdAt = now,
                completedAt = null,
                startAt = now
            ),
            parameters = listOf(
                Parameter(11, 1, "Distance", ParameterType.FLOAT.value, "km"),
                Parameter(12, 1, "Pace", ParameterType.FLOAT.value, "min/km"),
                Parameter(13, 1, "Heart Rate", ParameterType.INTEGER.value, "bpm"),
            ),
            parameterValues = listOf(
                WorkoutParameter(1, 112, 11, 12.4f, null, null),
                WorkoutParameter(2, 112, 12, 5.8f, null, null),
                WorkoutParameter(3, 112, 13, null, 160, null),
            ),
            onBack = {},
            onDelete = {},
        )
    }
}

@Preview
@Composable
private fun WorkoutDetailEmptyPreview() {
    Theme {
        WorkoutDetailContent(
            workout = null,
            goal = null,
            parameters = emptyList(),
            parameterValues = emptyList(),
            onBack = {},
            onDelete = {},
        )
    }
}
