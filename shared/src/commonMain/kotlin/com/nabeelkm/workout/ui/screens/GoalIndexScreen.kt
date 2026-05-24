package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun GoalIndexScreen(
    viewModel: GoalIndexViewModel = koinViewModel(),
    navigator: Navigator
) {
    GoalIndexScreen(
        goalsStateFlow = viewModel.goalsState,
        navigator
    )
}
@Composable
fun GoalIndexScreen(
    goalsStateFlow: StateFlow<List<Goal>>,
    navigator: Navigator
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
        val goals by goalsStateFlow.collectAsStateWithLifecycle()
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Active",
                Modifier.padding(bottom = 12.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(goals.size) { idx ->
                    val goal = goals[idx]
                    GoalCard(goal)
                }
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
fun GoalCard(goal: Goal) {
    Card(
        modifier = Modifier.border(1.dp, ThemeColor.border, RoundedCornerShape(10.dp)).fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ThemeColor.onBackground
        ),
    ) {
        var displayText = ""
        if (goal.startAt == null && goal.completedAt == null) {
            displayText = "Target dates unset"
        } else if (goal.startAt != null && goal.completedAt != null) {
            val startedText = ShortMonthDateFormat.format(goal.startAt.toLocalDatetime())
            val endText = ShortMonthDateFormat.format(goal.completedAt.toLocalDatetime())
            displayText = "${startedText} - ${endText}"
        } else if (goal.startAt != null) {
            val startedText = ShortMonthDateFormat.format(goal.startAt.toLocalDatetime())
            displayText = "Started ${startedText}"
        } else if (goal.completedAt != null) {
            val endText = ShortMonthDateFormat.format(goal.completedAt.toLocalDatetime())
            displayText = "Ended ${endText}"
        }
        
        Column(
            Modifier.padding(16.dp)
        ) {
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
            )
        )
    ).asStateFlow()
    Theme {
        GoalIndexScreen(
            goalsStateFlow,
            Navigator(SnapshotStateList())
        )
    }
}