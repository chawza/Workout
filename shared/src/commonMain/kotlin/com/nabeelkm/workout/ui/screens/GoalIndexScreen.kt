package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Instant


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
                }
            )
        }
    ) {
        val goals by goalsStateFlow.collectAsStateWithLifecycle()

        LazyRow {
            items(goals.size) { idx ->
                val goal = goals[idx]
                Card {
                    Column {
                        Row {
                            Column {
                                Text(
                                    goal.name
                                )
                                Text(
                                    ""
                                )

                            }
                            Button(
                                onClick = {

                                }
                            ) {
                                Text(
                                    goal.getStatus().label
                                )
                            }
                        }

                    }
                }
            }
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
                startAt = null
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