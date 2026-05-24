package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.LocalAutofillHighlightColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.theme.ThemeColor
import com.nabeelkm.workout.viewmodel.FormState
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Month
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant


@Composable
fun GoalFormScreen(
    viewModel: GoalFormViewModel = koinViewModel(),
    navigator: Navigator
) {
    GoalFormScreen(
        onAdd = {
            // TODO: save goal and go back
            navigator.goBack()
        },
        formStateFlow = viewModel.formState,
        onCancel = {
            navigator.goBack()
        }
    )
}
@Composable
fun GoalFormScreen(
    onAdd: (Goal) -> Unit,
    formStateFlow: StateFlow<FormState>,
    onCancel: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onStartDateChange: (Long?) -> Unit = {},
    onNotesChange: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // TODO: add border bottom as seperator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "New Goal",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = ThemeColor.primary
                            ),
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(18.dp, 10.dp)
                        ){
                            Text("Cancel")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.background)
            )
        }
    ) { innerPadding ->
        val formState by formStateFlow.collectAsState()
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val showDateModal = remember { mutableStateOf(false) }
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Goal Name",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.goalName,
                    onValueChange = onNameChange,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemeColor.primary,
                        unfocusedBorderColor = ThemeColor.border
                    ),
                    placeholder = {
                        Text("e.g. Run a half marathon")
                    }
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ThemeColor.border, RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = ThemeColor.onBackground,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {

                    Text(
                        "Parameters",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )

                    if (formState.parameters.isNotEmpty()) {
                        ParameterLists(parameters = formState.parameters)
                    } else {
                        Text(
                            "No parameters yet. Add one to track metrics for this goal.",
                            modifier = Modifier.padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodySmall.copy(ThemeColor.textGrey)
                        )
                    }


                    // TODO: dashed borders stroke
                    // TODO: hover using coral accent
                    OutlinedButton(
                        onClick = {

                        },
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ThemeColor.border)
                    ) {
                        Text("+ Add Parameter")
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Start Date",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.goalName,
                    onValueChange = {},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemeColor.primary,
                        unfocusedBorderColor = ThemeColor.border
                    ),
                    placeholder = {
                        Text("e.g. Run a half marathon")
                    }
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Notes",
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.goalName,
                    onValueChange = onNotesChange,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ThemeColor.primary,
                        unfocusedBorderColor = ThemeColor.border
                    ),
                    placeholder = {
                        Text("Why this goal? What's the plan?")
                    },
                    minLines = 2
                )
            }
            
            if (showDateModal.value) {
                DatePickerModal(
                    onDateSelected = onStartDateChange,
                    onDismiss = {
                        showDateModal.value = false
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {
                Button(
                    modifier = Modifier.weight(1F),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(18.dp, 10.dp),
                    onClick = onCancel,
                ) {
                    Text(
                        "Cancel",
                    )
                }
                Button(
                    modifier = Modifier.weight(1F),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeColor.primary,
                        contentColor = ThemeColor.white
                    ),
                    contentPadding = PaddingValues(18.dp, 10.dp),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        // TODO: check is validate
                        onAdd(
                            Goal(
                                0,
                                formState.goalName,
                                GoalStatus.NEW.value,
                                Clock.System.now().toEpochMilliseconds(),
                                null,
                                formState.startAt,
                            )
                        )
                    },
                ) {
                    Text("Save Goal")
                }
            }
        }
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ParameterLists(modifier: Modifier = Modifier, parameters: List<Parameter>) {
    LazyRow(
        modifier = modifier.fillMaxWidth()
    ) {
        items(parameters.size) {
            val parameters = parameters[it]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column {
                    Text("Name")
                    // TODO custom text filed with not padding
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp),
                        value = parameters.name,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemeColor.primary,
                            unfocusedBorderColor = ThemeColor.border,
                            unfocusedContainerColor = ThemeColor.background
                        ),
                    )
                }
                Column {
                    val showInputMenu = remember { mutableStateOf(false) }
                    val selectedType = remember { mutableStateOf(ParameterType.STRING) }

                    Text("Type")
                    OutlinedTextField(
                        modifier = Modifier.width(80.dp).clickable {
                            showInputMenu.value = true
                        },
                        value = parameters.name,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemeColor.primary,
                            unfocusedBorderColor = ThemeColor.border,
                            unfocusedContainerColor = ThemeColor.background
                        ),
                    )
                    InputMenu(
                        showInputMenu.value,
                        choices = ParameterType.entries.toList(),
                        onChange = {
                            selectedType.value = it
                        }
                    )
                }
                Column {
                    Text("Unit")
                    OutlinedTextField(
                        value = parameters.name,
                        onValueChange = {},
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ThemeColor.primary,
                            unfocusedBorderColor = ThemeColor.border,
                            unfocusedContainerColor = ThemeColor.background
                        ),
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Text("x")
                    }
                }
            }
        }
    }
}

@Composable
fun <T> InputMenu(show: Boolean, choices: List<T>, onChange: (T) -> Unit, onDismissRequest: () -> Unit = {}) {
    if (show) {
        DropdownMenu(
            show,
            onDismissRequest = onDismissRequest
        ) {
            choices.map { choice ->
                DropdownMenuItem(
                    text = { Text(choice.toString()) },
                    onClick = { onChange(choice) }
                )
            }
        }
    }
}


@Preview
@Composable
fun GoalFormScreenPreview() {
    val formState = MutableStateFlow(
        FormState(
            parameters = listOf<Parameter>()
        )
    )
    Theme {
        GoalFormScreen(
            onAdd = {},
            formStateFlow = formState,
        )
    }
}

@Preview
@Composable
fun ParameterListsPreview() {
    Card(
        modifier = Modifier.background(ThemeColor.onBackground).padding(16.dp)
    ) {
        ParameterLists(
            parameters = listOf(
                Parameter(
                    0,
                    0,
                    "LMAO",
                    ParameterType. STRING.value,
                )
            )
        )
    }
}