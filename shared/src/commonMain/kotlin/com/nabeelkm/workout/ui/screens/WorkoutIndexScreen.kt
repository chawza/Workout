package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nabeelkm.workout.entity.Workout
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppRadius
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.AppText
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.Card

@Composable
fun WorkoutIndexScreen(
    appNavigator: AppNavigator
) {
    WorkoutIndexContent(
        onAddWorkout = {
//            appNavigator.navigate()
        }
    )
}

@Composable
private fun WorkoutIndexContent(
    onAddWorkout: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Workouts"
                        )

                        val colors = SegmentedButtonDefaults.colors(
                            inactiveContainerColor = AppColor.bg,
                            activeContainerColor = AppColor.surface
                        )
                        val padding = PaddingValues(0.dp)
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                onClick = {

                                },
                                shape = RoundedCornerShape(
                                    topStart = AppRadius.sm,
                                    topEnd = 0.dp,
                                    bottomStart = AppRadius.sm,
                                    bottomEnd = 0.dp
                                ),
                                selected = true,
                                colors = colors,
                                icon = {},
                                contentPadding = padding
                            ) {
                                Text(
                                    text = "List",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = AppText.sm
                                )
                            }
                            SegmentedButton(
                                onClick = {

                                },
                                shape = RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = AppRadius.sm,
                                    bottomStart = 0.dp,
                                    bottomEnd = AppRadius.sm
                                ),
                                selected = false,
                                colors = colors,
                                icon = {},
                                contentPadding = padding,
                            ) {
                                Text(
                                    text = "Calendar",
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(AppSpace.s4),
                verticalArrangement = Arrangement.spacedBy(AppSpace.s4)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onAddWorkout,
                    shape = RoundedCornerShape(AppRadius.md),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "+ Log Workout",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpace.s4)
                ) {
                    QuickSummaryCard(
                        modifier = Modifier.weight(1F),
                        "Workouts",
                        "3"
                    )
                    QuickSummaryCard(
                        modifier = Modifier.weight(1F),
                        "Duration",
                        "2.8H"
                    )
                    QuickSummaryCard(
                        modifier = Modifier.weight(1F),
                        "Goals",
                        "3"
                    )
                    QuickSummaryCard(
                        modifier = Modifier.weight(1F),
                        "KM",
                        "20.4"
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickSummaryCard(modifier: Modifier = Modifier, title: String, content: String) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(AppSpace.s3).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpace.s2)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = content.take(4),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = AppText.lg,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title.take(8),
                style = MaterialTheme.typography.bodySmall,
                color = AppColor.muted,
                textAlign = TextAlign.Center,
                fontSize = AppText.xs,
            )
        }
    }
}

@Preview
@Composable
fun WorkoutIndexPreview() {
    Theme {
        WorkoutIndexContent(
            onAddWorkout = {}
        )
    }
}
