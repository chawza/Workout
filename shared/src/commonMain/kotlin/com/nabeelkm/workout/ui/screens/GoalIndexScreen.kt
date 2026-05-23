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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.theme.ThemeColor
import com.nabeelkm.workout.viewmodel.GoalIndexViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GoalIndexScreen(
    viewModel: GoalIndexViewModel = koinViewModel()
) {
    Theme {
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
            val goals by viewModel.goalsState.collectAsStateWithLifecycle()

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
}