package com.nabeelkm.workout.ui.screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.database.AppDatabase
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.navigation.AppRoute
import com.nabeelkm.workout.navigation.HomeNavigator
import com.nabeelkm.workout.repository.GoalRepository
import com.nabeelkm.workout.repository.WorkoutRepository
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppRadius
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.AppText
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.Card
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.utils.formatDate
import com.nabeelkm.workout.viewmodel.WorkoutIndexViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.workout_icon
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

private enum class WorkoutView { LIST, CALENDAR }

@Composable
fun WorkoutIndexScreen(
    viewModel: WorkoutIndexViewModel = koinViewModel(),
    navigator: HomeNavigator
) {
    val workoutList = viewModel.recentWorkoutFlow.collectAsStateWithLifecycle(
        initialValue = listOf(),
    )

    val goalRepository: GoalRepository = koinInject()
    val allGoals = goalRepository.allGoalsFlow.collectAsStateWithLifecycle(initialValue = listOf())

    WorkoutIndexContent(
        workouts = workoutList.value,
        goals = allGoals.value,
        onLogWorkout = { navigator.navigate(AppRoute.LogWorkout()) },
        onOpenWorkout = { navigator.navigate(AppRoute.WorkoutDetail(it.id)) },
    )
}

@Composable
private fun WorkoutIndexContent(
    workouts: List<Workout>,
    goals: List<Goal>,
    onLogWorkout: () -> Unit,
    onOpenWorkout: (Workout) -> Unit,
) {
    var selectedView by remember { mutableStateOf(WorkoutView.LIST) }
    val hasActiveGoals = goals.any { it.getStatus() == GoalStatus.ACTIVE }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Workouts")

                        val colors = SegmentedButtonDefaults.colors(
                            inactiveContainerColor = AppColor.bg,
                            activeContainerColor = AppColor.surface
                        )
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                onClick = { selectedView = WorkoutView.LIST },
                                shape = RoundedCornerShape(
                                    topStart = AppRadius.sm,
                                    topEnd = 0.dp,
                                    bottomStart = AppRadius.sm,
                                    bottomEnd = 0.dp
                                ),
                                selected = selectedView == WorkoutView.LIST,
                                colors = colors,
                                icon = {},
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "List",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = AppText.sm
                                )
                            }
                            SegmentedButton(
                                onClick = { selectedView = WorkoutView.CALENDAR },
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = AppRadius.sm,
                                    bottomStart = 0.dp,
                                    bottomEnd = AppRadius.sm
                                ),
                                selected = selectedView == WorkoutView.CALENDAR,
                                colors = colors,
                                icon = {},
                                contentPadding = PaddingValues(0.dp),
                            ) {
                                Text(
                                    "Calendar",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = AppText.sm
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.bg,
                )
            )
        }
    ) { innerPadding ->
        val now = remember { Clock.System.now() }
        val sevenDaysAgo = remember { now.minus(6.days) }
        val sorted = remember(workouts) {
            workouts.sortedByDescending { it.time }
        }
        val thisWeek = remember(sorted, sevenDaysAgo) {
            sorted.filter { Instant.fromEpochMilliseconds(it.time) >= sevenDaysAgo }
        }
        val earlier = remember(sorted, sevenDaysAgo) {
            sorted.filter { Instant.fromEpochMilliseconds(it.time) < sevenDaysAgo }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(AppSpace.s4),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Log Workout button
            item {
                if (hasActiveGoals) {
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onLogWorkout,
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
                    ) {
                        Text(
                            "+ Log Workout",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AppColor.border, RoundedCornerShape(AppRadius.md))
                            .background(AppColor.surface, RoundedCornerShape(AppRadius.md))
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Create a goal first to start logging workouts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColor.muted
                        )
                    }
                }
                Spacer(Modifier.height(AppSpace.s5))
            }

            // Week summary
            item {
                WeekSummary(workouts = workouts, goals = goals)
                Spacer(Modifier.height(AppSpace.s5))
            }

            if (selectedView == WorkoutView.LIST) {
                if (thisWeek.isNotEmpty()) {
                    item { WorkoutSectionHeader("THIS WEEK") }
                    items(items = thisWeek, key = { "tw_${it.id}" }) { workout ->
                        WorkoutRow(
                            workout = workout,
                            goal = goals.find { it.id == workout.goalId },
                            onClick = { onOpenWorkout(workout) }
                        )
                    }
                    item { Spacer(Modifier.height(AppSpace.s5)) }
                }

                if (earlier.isNotEmpty()) {
                    item { WorkoutSectionHeader("EARLIER") }
                    items(items = earlier, key = { "e_${it.id}" }) { workout ->
                        WorkoutRow(
                            workout = workout,
                            goal = goals.find { it.id == workout.goalId },
                            onClick = { onOpenWorkout(workout) }
                        )
                    }
                }

                if (workouts.isEmpty()) {
                    item { WorkoutEmptyState() }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .border(1.dp, AppColor.border, RoundedCornerShape(AppRadius.md))
                            .background(AppColor.surface, RoundedCornerShape(AppRadius.md)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Calendar view coming soon",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColor.muted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekSummary(workouts: List<Workout>, goals: List<Goal>) {
    val now = remember { Clock.System.now() }
    val sevenDaysAgo = remember { now.minus(6.days) }
    val inWeek = remember(workouts, sevenDaysAgo) {
        workouts.filter { Instant.fromEpochMilliseconds(it.time) >= sevenDaysAgo }
    }
    val totalMinutes = remember(inWeek) {
        inWeek.sumOf { (it.duration ?: 0L) }
    }
    val activeGoals = remember(goals) {
        goals.count { it.getStatus() == GoalStatus.ACTIVE }
    }
    val durationDisplay = remember(totalMinutes) {
        if (totalMinutes >= 60) {
            val hours = totalMinutes.toDouble() / 60.0
            val whole = hours.toLong()
            val frac = ((hours - whole) * 10).toLong()
            if (frac == 0L) "${whole}h" else "${whole}.${frac}h"
        } else {
            "${totalMinutes}m"
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(AppSpace.s3)
    ) {
        QuickSummaryCard(Modifier.weight(1f), "Workouts", "${inWeek.size}")
        QuickSummaryCard(Modifier.weight(1f), "Duration", durationDisplay)
        QuickSummaryCard(Modifier.weight(1f), "Goals", "$activeGoals")
        QuickSummaryCard(Modifier.weight(1f), "km", "0")
    }
}

@Composable
private fun QuickSummaryCard(modifier: Modifier = Modifier, title: String, content: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(vertical = AppSpace.s3, horizontal = AppSpace.s2)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = content.take(6),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = AppText.xl,
                color = AppColor.fg,
                letterSpacing = (-0.02).sp
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = AppColor.muted,
                textAlign = TextAlign.Center,
                fontSize = AppText.xs,
            )
        }
    }
}

@Composable
private fun WorkoutSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium.copy(
            color = AppColor.muted,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.04.sp
        ),
        modifier = Modifier.padding(bottom = AppSpace.s2)
    )
}

@Composable
private fun WorkoutRow(workout: Workout, goal: Goal?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppSpace.s4),
        horizontalArrangement = Arrangement.spacedBy(AppSpace.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(AppRadius.sm))
                .border(1.dp, AppColor.border, RoundedCornerShape(AppRadius.sm))
                .background(AppColor.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.workout_icon),
                contentDescription = null,
                tint = AppColor.accent
            )
        }

        // Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = workout.notes?.takeIf { it.isNotBlank() } ?: "Workout",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = goal?.name ?: "—",
                style = MaterialTheme.typography.bodySmall,
                color = AppColor.muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Meta date
        Text(
            text = relativeDate(workout.time),
            style = MaterialTheme.typography.labelSmall,
            color = AppColor.muted,
            fontFamily = FontFamily.Monospace
        )
    }
    HorizontalDivider(thickness = 1.dp, color = AppColor.borderSoft)
}

@Composable
private fun WorkoutEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpace.s8, horizontal = AppSpace.s4),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpace.s3)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(AppColor.accentSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(26.dp),
                painter = painterResource(Res.drawable.workout_icon),
                contentDescription = null,
                tint = AppColor.accent
            )
        }
        Text(
            "No workouts yet",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = AppColor.fg
        )
        Text(
            "Tap the + button to log your first one.",
            style = MaterialTheme.typography.bodySmall,
            color = AppColor.muted
        )
    }
}

private fun relativeDate(epochMillis: Long): String {
    val now = Clock.System.now()
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val diff = now - instant
    val days = diff.inWholeDays
    return when {
        days <= 0L -> "Today"
        days == 1L -> "Yesterday"
        days < 7L -> "${days}d ago"
        else -> formatDate(epochMillis)
    }
}

@Preview
@Composable
private fun WorkoutIndexPreview() {
    val now = Clock.System.now().toEpochMilliseconds()
    val day = 86_400_000L

    val goals = listOf(
        Goal(1, "Half Marathon Prep", GoalStatus.ACTIVE.value, now - 30 * day, null, now - 30 * day),
        Goal(2, "Daily 5K", GoalStatus.ACTIVE.value, now - 45 * day, null, now - 45 * day),
        Goal(3, "Upper Body Strength", GoalStatus.ACTIVE.value, now - 20 * day, null, now - 20 * day),
    )
    val workouts = listOf(
        Workout(112, 1, now, 72, "Long Run"),
        Workout(111, 3, now - 2 * day, 52, "Arm Day"),
        Workout(110, 1, now - 4 * day, 44, "Tempo Run"),
        Workout(109, 1, now - 7 * day, 32, "Morning Run"),
        Workout(108, 3, now - 8 * day, 50, "Arm Day"),
        Workout(107, 2, now - 10 * day, 40, "Evening Walk"),
        Workout(106, 1, now - 13 * day, 28, "Morning Run"),
    )

    Theme {
        WorkoutIndexContent(
            workouts = workouts,
            goals = goals,
            onLogWorkout = {},
            onOpenWorkout = {},
        )
    }
}

@Preview
@Composable
private fun WorkoutIndexEmptyPreview() {
    Theme {
        WorkoutIndexContent(
            workouts = emptyList(),
            goals = emptyList(),
            onLogWorkout = {},
            onOpenWorkout = {},
        )
    }
}
