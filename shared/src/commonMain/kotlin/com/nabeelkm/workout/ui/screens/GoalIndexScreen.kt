package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.Screen
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.theme.ThemeColor
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant


@Composable
fun GoalIndexScreen(
    viewModel: GoalIndexViewModel = koinViewModel(),
    navigator: Navigator
) {
    GoalIndexScreen(
        activeGoalsStateFlow = viewModel.activeGoals,
        newGoalsStateFlow = viewModel.newGoals,
        navigator = navigator,
        onDelete = viewModel::deleteGoal,
        onEdit = { goal ->
            navigator.navigate(Screen.GoalEditForm(goalId = goal.id))
        }
    )
}
@Composable
fun GoalIndexScreen(
    activeGoalsStateFlow: StateFlow<List<Goal>>,
    newGoalsStateFlow: StateFlow<List<Goal>>,
    navigator: Navigator,
    onDelete: (Goal) -> Unit = {},
    onEdit: (Goal) -> Unit = {}
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
                            "Goals",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = ThemeColor.primary
                            ),
                            onClick = {
                                navigator.navigate(
                                    Screen.GoalAddForm
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(18.dp, 10.dp)
                        ){
                            Text("+ New Goal")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.background)
            )
        }
    ) { innerPadding ->
        val activeGoals by activeGoalsStateFlow.collectAsStateWithLifecycle()
        val newGoals by newGoalsStateFlow.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Active",
                    Modifier.padding(bottom = 12.dp)
                )
            }
            items(items = activeGoals, key = { goal -> "active_${goal.id}" }) { goal ->
                GoalCard(
                    goal,
                    onDelete = onDelete,
                    onEdit = onEdit,
                    navigateToDetail = {
                        navigator.navigate(Screen.GoalIndexDetail(goal.id))
                    }
                )
            }

            item {
                Spacer(Modifier.height(20.dp))
            }

            item {
                Text(
                    "New",
                    Modifier.padding(bottom = 12.dp)
                )
            }
            items(items = newGoals, key = { goal -> "new_${goal.id}" }) { goal ->
                GoalCard(
                    goal,
                    onDelete = onDelete,
                    onEdit = onEdit,
                    navigateToDetail = {
                        navigator.navigate(Screen.GoalIndexDetail(goal.id))
                    }
                )
            }
        }

    }
}


fun Long.toLocalDatetime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

val ShortMonthDateFormat = LocalDateTime.Format {
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    day()
}

// TODO: move to primitive
@Composable
fun Pill(text: String, modifier: Modifier = Modifier, color: Color) {
    Text(
        text = text,
        modifier = modifier
            .background(color.copy(alpha = 0.1F), shape = RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 2.dp),
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun DangerTextButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHover by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val color = if (isHover || isPressed) ThemeColor.danger else ThemeColor.textBlack
    val containerColor = if (isHover || isPressed) color.copy(alpha = 0.1F) else Color.Unspecified

    TextButton(
        modifier = modifier,
        interactionSource = interactionSource,
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = color,
        ),
        contentPadding = PaddingValues(12.dp, 6.dp)
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun GoalCard(goal: Goal, onDelete: (Goal) -> Unit = {}, onEdit: (Goal) -> Unit = {}, navigateToDetail: (Goal) -> Unit = {}) {
    Card(
        modifier = Modifier
            .clickable {
                navigateToDetail(goal)
            }
            .border(1.dp, ThemeColor.border, RoundedCornerShape(10.dp))
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColor.onBackground
        ),
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var displayText = ""
            if (goal.startAt == null && goal.completedAt == null) {
                displayText = "Target dates unset"
            } else if (goal.startAt != null && goal.completedAt != null) {
                val startedText = ShortMonthDateFormat.format(goal.startAt.toLocalDatetime())
                val endText = ShortMonthDateFormat.format(goal.completedAt.toLocalDatetime())
                displayText = "$startedText - $endText"
            } else if (goal.startAt != null) {
                val startedText = ShortMonthDateFormat.format(goal.startAt.toLocalDatetime())
                displayText = "Started $startedText"
            } else if (goal.completedAt != null) {
                val endText = ShortMonthDateFormat.format(goal.completedAt.toLocalDatetime())
                displayText = "Ended $endText"
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        goal.name,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "$displayText - 0 Parameters",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.weight(1F))
                val status = goal.getStatus()
                val labelColor = when(status) {
                    GoalStatus.NEW -> Color.Blue
                    GoalStatus.ACTIVE -> ThemeColor.success
                    GoalStatus.COMPLETED -> ThemeColor.muted
                }
                Pill(
                    text = status.label,
                    color = labelColor
                )
            }

            HorizontalDivider(thickness = 1.dp, color = ThemeColor.borderSoft)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        onEdit(goal)
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, ThemeColor.border),
                    contentPadding = PaddingValues(12.dp, 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = ThemeColor.textBlack)
                ) {
                    Text("Edit", style = MaterialTheme.typography.bodySmall)
                }
                DangerTextButton(
                    "Delete",
                    onClick = {
                        onDelete(goal)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun GoalCardPreview() {
    Theme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalCard(
                Goal(
                    0,
                    "Running",
                    status = GoalStatus.NEW.value,
                    createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                    completedAt = null,
                    startAt = 1750611600000
                )
            )
            GoalCard(
                Goal(
                    0,
                    "Badminton",
                    status = GoalStatus.ACTIVE.value,
                    createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                    startAt = 1750611600000,
                    completedAt = 1750611600000,
                )
            )
            GoalCard(
                Goal(
                    0,
                    "Rucking",
                    status = GoalStatus.COMPLETED.value,
                    createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                    completedAt = null,
                    startAt = 1750611600000
                )
            )
        }
    }
}

@Preview
@Composable
fun GoalIndexScreenPreview() {
    val goalsStateFlow = MutableStateFlow(
        listOf(
            Goal(
                0,
                "Running",
                status = GoalStatus.ACTIVE.value,
                createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                completedAt = null,
                startAt = 1750611600000
            ),
            Goal(
                0,
                "Rucking",
                status = GoalStatus.ACTIVE.value,
                createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                completedAt = null,
                startAt = 1750611600000
            )
        )
    ).asStateFlow()
    val newStateFlow = MutableStateFlow(
        listOf(
            Goal(
                0,
                "Running",
                status = GoalStatus.NEW.value,
                createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                completedAt = null,
                startAt = 1750611600000
            ),
            Goal(
                0,
                "Rucking",
                status = GoalStatus.NEW.value,
                createdAt = Instant.parse("2024-05-24T10:30:00Z").toEpochMilliseconds(),
                completedAt = null,
                startAt = 1750611600000
            )
        )
    ).asStateFlow()
    Theme {
        GoalIndexScreen(
            goalsStateFlow,
            newStateFlow,
            Navigator(SnapshotStateList())
        )
    }
}