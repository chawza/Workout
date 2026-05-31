package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.checkScrollableContainerConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.room.util.TableInfo
import com.nabeelkm.workout.Screen
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.theme.ThemeColor
import com.nabeelkm.workout.ui.components.Card
import kotlin.math.sin

@Composable
fun SettingsScreen() {
    SettingsScreenContent()
}

@Composable
private fun SettingsScreenContent() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Settings",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.background)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val checkState = remember { mutableStateOf(false) }

            SectionHeader("Appearance".uppercase())
            Card {
                ConfigRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Dark Mode",
                    description = "Use dark theme throughout the app",
                    actions = {
                        Switch(
                            checked = checkState.value,
                            onCheckedChange = { checkState.value = !checkState.value },
                            colors = SwitchDefaults.colors(
                                uncheckedTrackColor = ThemeColor.border,
                                uncheckedBorderColor = ThemeColor.border,
                                uncheckedThumbColor = ThemeColor.white,
                                uncheckedIconColor = ThemeColor.white,
                                checkedTrackColor = ThemeColor.primary,
                                checkedBorderColor = ThemeColor.primary,
                                checkedThumbColor = ThemeColor.white,
                                checkedIconColor = ThemeColor.white,
                            ),
                        )
                    }
                )
            }

            SectionHeader("Home Screen".uppercase())
            Card {
                ConfigRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Default Screen",
                    description = "Which view opens first on landscape / desktop",
                    actions = {
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                selected = true,
                                onClick = {},
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 8.dp),
                                label = { Text("List") },
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = ThemeColor.background
                                ),
                                icon = {},
                                contentPadding = PaddingValues(0.dp)
                            )
                            SegmentedButton(
                                selected = false,
                                onClick = {},
                                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 0.dp),
                                label = { Text("Calendar") },
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = ThemeColor.background
                                ),
                                icon = {},
                                contentPadding = PaddingValues(0.dp)
                            )
                        }
                    }
                )
            }

            SectionHeader("Data".uppercase())
            Card {
                ConfigRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Default Screen",
                    description = "Download all workouts and goals as JSON",
                    actions = {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ThemeColor.onBackground,
                                contentColor = ThemeColor.textBlack,
                            ),
                            border = BorderStroke(width = 1.dp, color = ThemeColor.border)
                        ) {
                            Text("Export")
                        }
                    }
                )

                HorizontalDivider(thickness = 1.dp, color = ThemeColor.border, modifier = Modifier.padding(horizontal = 16.dp))

                ConfigRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Clear All Data",
                    description = "Permanently delete all workouts and goals",
                    actions = {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ThemeColor.danger,
                                contentColor = ThemeColor.onBackground,
                            ),
                            border = null
                        ) {
                            Text("Export")
                        }
                    }
                )
            }

            SectionHeader("About".uppercase())
            Card {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                ) {
                    Text(
                        text = "Workout Tracker v0.1.0",
                        color = ThemeColor.textGrey,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "KMP Project",
                        color = ThemeColor.textGrey,
                        fontSize = 14.sp
                    )
                }
            }

        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        color = ThemeColor.muted,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun ConfigRow(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    actions: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 14.sp,
                color = ThemeColor.textGrey
            )
        }
        actions()
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    Theme {
        SettingsScreenContent()
    }
}
