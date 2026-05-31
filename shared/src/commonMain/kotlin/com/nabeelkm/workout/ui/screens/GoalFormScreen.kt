package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.navigation.Navigator
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.theme.ThemeColor
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.viewmodel.FormState
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock


@Composable
fun GoalFormScreen(
    viewModel: GoalFormViewModel = koinViewModel(),
    navigator: Navigator,
) {
    val existingGoal by viewModel.existingGoal.collectAsState()
    val formState by viewModel.formState.collectAsState()

    GoalFormContent(
        existing = existingGoal,
        formState = formState,
        onAdd = viewModel::addGoal,
        onEdit = viewModel::onUpdate,
        onCancel = { navigator.goBack() },
        onNameChange = viewModel::onNameChanges,
        onStartDateChange = viewModel::onStartDateChanges,
        onStartTimeChange = viewModel::onStartTimeChanges,
        onCompletedDateChange = viewModel::onCompletedDateChanges,
        onCompletedTimeChange = viewModel::onCompletedTimeChanges,
        onNotesChange = viewModel::onNotesChanges
    )
}

@Composable
private fun GoalFormContent(
    existing: Goal? = null,
    formState: FormState,
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onStartDateChange: (Long?) -> Unit = {},
    onStartTimeChange: (Int, Int) -> Unit = { _, _ -> },
    onCompletedDateChange: (Long?) -> Unit = {},
    onCompletedTimeChange: (Int, Int) -> Unit = { _, _ -> },
    onNotesChange: (String) -> Unit = {}
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
                            text = if (existing != null) "Update Goal"  else "New Goal" ,
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
                        ) {
                            Text("Cancel")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ThemeColor.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val showStartDateModal = remember { mutableStateOf(false) }
            val showStartTimeModal = remember { mutableStateOf(false) }
            val showCompletedDateModal = remember { mutableStateOf(false) }
            val showCompletedTimeModal = remember { mutableStateOf(false) }

            // Goal Name
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

            // Parameters Card
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
                        onClick = { },
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, ThemeColor.border)
                    ) {
                        Text("+ Add Parameter")
                    }
                }
            }

            // Start Date
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Start Time",
                    style = MaterialTheme.typography.bodySmall
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartDateModal.value = true }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formState.startAtDisplay,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = ThemeColor.border,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        placeholder = {
                            Text("Select a date")
                        }
                    )
                }
            }

            // Completed Date
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Completed Time",
                    style = MaterialTheme.typography.bodySmall
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCompletedDateModal.value = true }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = formState.completedAtDisplay,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = ThemeColor.border,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        placeholder = {
                            Text("Select a date (optional)")
                        }
                    )
                }
            }

            // Notes
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
                    value = formState.notes,
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

            // Start Date Modal
            if (showStartDateModal.value) {
                DatePickerModal(
                    onDateSelected = {
                        onStartDateChange(it)
                        if (it != null) {
                            showStartTimeModal.value = true
                        }
                    },
                    onDismiss = {
                        showStartDateModal.value = false
                    }
                )
            }

            // Start Time Modal
            if (showStartTimeModal.value) {
                val tz = TimeZone.currentSystemDefault()
                val startAt = formState.startAt
                val currentHour = if (startAt != null) {
                    kotlin.time.Instant.fromEpochMilliseconds(startAt).toLocalDateTime(tz).hour
                } else {
                    val nowMillis = Clock.System.now().toEpochMilliseconds()
                    kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).hour
                }
                val currentMinute = if (startAt != null) {
                    kotlin.time.Instant.fromEpochMilliseconds(startAt).toLocalDateTime(tz).minute
                } else {
                    val nowMillis = Clock.System.now().toEpochMilliseconds()
                    kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).minute
                }
                TimePickerModal(
                    initialHour = currentHour,
                    initialMinute = currentMinute,
                    onTimeSelected = { hour, minute ->
                        onStartTimeChange(hour, minute)
                    },
                    onDismiss = {
                        showStartTimeModal.value = false
                    }
                )
            }

            // Completed Date Modal
            if (showCompletedDateModal.value) {
                DatePickerModal(
                    onDateSelected = {
                        onCompletedDateChange(it)
                        if (it != null) {
                            showCompletedTimeModal.value = true
                        }
                    },
                    onDismiss = {
                        showCompletedDateModal.value = false
                    }
                )
            }

            // Completed Time Modal
            if (showCompletedTimeModal.value) {
                val tz = TimeZone.currentSystemDefault()
                val completedAt = formState.completedAt
                val currentHour = if (completedAt != null) {
                    kotlin.time.Instant.fromEpochMilliseconds(completedAt).toLocalDateTime(tz).hour
                } else {
                    val nowMillis = Clock.System.now().toEpochMilliseconds()
                    kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).hour
                }
                val currentMinute = if (completedAt != null) {
                    kotlin.time.Instant.fromEpochMilliseconds(completedAt)
                        .toLocalDateTime(tz).minute
                } else {
                    val nowMillis = Clock.System.now().toEpochMilliseconds()
                    kotlin.time.Instant.fromEpochMilliseconds(nowMillis).toLocalDateTime(tz).minute
                }
                TimePickerModal(
                    initialHour = currentHour,
                    initialMinute = currentMinute,
                    onTimeSelected = { hour, minute ->
                        onCompletedTimeChange(hour, minute)
                    },
                    onDismiss = {
                        showCompletedTimeModal.value = false
                    }
                )
            }

            // Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1F),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(18.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemeColor.white,
                        contentColor = ThemeColor.textBlack
                    ),
                    border = BorderStroke(1.dp, ThemeColor.border),
                    onClick = onCancel,
                ) {
                    Text("Cancel")
                }
                PrimaryButton(
                    modifier = Modifier.weight(1F),
                    onClick = {
                        if (existing == null) onAdd()
                        else onEdit()
                    },
                ) {
                    if(existing != null) {
                        Text("Update")
                    } else {
                        Text("Save Goal")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        },
        colors = DatePickerDefaults.colors(
            containerColor = ThemeColor.onBackground
        )
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialHour: Int = 0,
    initialMinute: Int = 0,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(timePickerState.hour, timePickerState.minute)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = ThemeColor.onBackground,
        text = {
            TimePicker(state = timePickerState)
        }
    )
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
fun <T> InputMenu(
    show: Boolean,
    choices: List<T>,
    onChange: (T) -> Unit,
    onDismissRequest: () -> Unit = {}
) {
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
fun GoalFormPreview() {
    Theme {
        GoalFormContent(
            formState = FormState(
                parameters = listOf()
            )
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
                    ParameterType.STRING.value,
                )
            )
        )
    }
}
