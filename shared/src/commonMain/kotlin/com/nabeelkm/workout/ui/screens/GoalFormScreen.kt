package com.nabeelkm.workout.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.GOAL_COLORS
import com.nabeelkm.workout.ui.components.GOAL_ICONS
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.ui.components.WorkoutIcons
import com.nabeelkm.workout.ui.components.goalIconVector
import com.nabeelkm.workout.ui.components.hexToColor
import com.nabeelkm.workout.viewmodel.FormState
import com.nabeelkm.workout.viewmodel.GoalFormViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock


@Composable
fun GoalFormScreen(
    goalId: Int? = null,
    viewModel: GoalFormViewModel = koinViewModel(),
    navigator: AppNavigator,
) {
    val existingGoal by viewModel.existingGoal.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val enabledIcons by viewModel.enabledIcons.collectAsStateWithLifecycle()

    LaunchedEffect(goalId) {
        if (goalId != null) {
            viewModel.loadGoal(goalId)
        } else {
            viewModel.resetStates()
        }
    }

    GoalFormContent(
        existing = existingGoal,
        formState = formState,
        enabledIcons = enabledIcons,
        onAdd = {
            viewModel.addGoal()
            navigator.goBack()
        },
        onEdit = {
            viewModel.onUpdate()
            navigator.goBack()
        },
        onCancel = { navigator.goBack() },
        onNameChange = viewModel::onNameChanges,
        onStartDateChange = viewModel::onStartDateChanges,
        onStartTimeChange = viewModel::onStartTimeChanges,
        onCompletedDateChange = viewModel::onCompletedDateChanges,
        onCompletedTimeChange = viewModel::onCompletedTimeChanges,
        onNotesChange = viewModel::onNotesChanges,
        onIconPickerOpened = viewModel::onIconPickerOpened,
        onIconChange = viewModel::onIconChanges,
        onColorChange = viewModel::onColorChanges,
    )
}

@Composable
private fun GoalFormContent(
    existing: Goal? = null,
    formState: FormState,
    enabledIcons: Set<String> = GOAL_ICONS.map { it.id }.toSet(),
    onAdd: () -> Unit = {},
    onEdit: () -> Unit = {},
    onCancel: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onStartDateChange: (Long?) -> Unit = {},
    onStartTimeChange: (Int, Int) -> Unit = { _, _ -> },
    onCompletedDateChange: (Long?) -> Unit = {},
    onCompletedTimeChange: (Int, Int) -> Unit = { _, _ -> },
    onNotesChange: (String) -> Unit = {},
    onIconPickerOpened: () -> Unit = {},
    onIconChange: (String) -> Unit = {},
    onColorChange: (String) -> Unit = {},
) {
    val showPicker = remember { mutableStateOf(false) }
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
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            onClick = onCancel,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(18.dp, 10.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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

            // Goal Name + icon/colour picker trigger
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Goal Name",
                    style = MaterialTheme.typography.bodySmall
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val goalColor = hexToColor(formState.colorHex)
                    Box(
                        modifier = Modifier
                            .size(width = 48.dp, height = 56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(goalColor.copy(alpha = 0.14f))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                            .clickable {
                                onIconPickerOpened()
                                showPicker.value = true
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = goalIconVector(formState.iconId),
                            contentDescription = "Choose icon and colour",
                            tint = goalColor,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = formState.goalName,
                        onValueChange = onNameChange,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        placeholder = {
                            Text("e.g. Run a half marathon")
                        }
                    )
                }
            }

            // Parameters Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        "Parameters",
                        style = MaterialTheme.typography.labelLarge
                    )

                    if (formState.parameters.isNotEmpty()) {
                        ParameterLists(parameters = formState.parameters)
                    } else {
                        Text(
                            "No parameters yet. Add one to track metrics for this goal.",
                            modifier = Modifier.padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }

                    // TODO: dashed borders stroke
                    // TODO: hover using coral accent
                    OutlinedButton(
                        onClick = { },
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
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
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
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
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    modifier = Modifier.weight(1F),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(18.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
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

        if (showPicker.value) {
            IconColorPicker(
                selectedIcon = formState.iconId,
                selectedColor = formState.colorHex,
                enabledIcons = enabledIcons,
                onIconChange = onIconChange,
                onColorChange = onColorChange,
                onDismiss = { showPicker.value = false },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun IconColorPicker(
    selectedIcon: String,
    selectedColor: String,
    enabledIcons: Set<String>,
    onIconChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val selected = hexToColor(selectedColor)
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Icon & Colour", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = WorkoutIcons.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            // Icon grid
            PickerSectionLabel("Icon")
            val icons = GOAL_ICONS.filter { it.id in enabledIcons }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                icons.chunked(4).forEach { rowIcons ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        rowIcons.forEach { def ->
                            val on = def.id == selectedIcon
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (on) selected.copy(alpha = 0.12f)
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        color = if (on) selected else MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(10.dp),
                                    )
                                    .clickable { onIconChange(def.id) }
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Icon(
                                    imageVector = def.image,
                                    contentDescription = def.displayName,
                                    tint = if (on) selected else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(22.dp),
                                )
                                Text(
                                    text = def.displayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (on) selected else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        repeat(4 - rowIcons.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }

            // Colour swatches
            PickerSectionLabel("Colour")
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                GOAL_COLORS.forEach { c ->
                    val on = c.hex.equals(selectedColor, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .then(
                                if (on) Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                else Modifier
                            )
                            .padding(if (on) 4.dp else 0.dp)
                            .clip(CircleShape)
                            .background(hexToColor(c.hex))
                            .clickable { onColorChange(c.hex) },
                    )
                }
            }

            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss,
            ) {
                Text("Done")
            }
        }
    }
}

@Composable
private fun PickerSectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
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
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                todayContentColor = MaterialTheme.colorScheme.primary,
                selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
            )
        )
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
        containerColor = MaterialTheme.colorScheme.surface,
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    selectorColor = MaterialTheme.colorScheme.primary,
                    clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.primary,
                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    periodSelectorSelectedContentColor = MaterialTheme.colorScheme.primary,
                    periodSelectorBorderColor = MaterialTheme.colorScheme.outline,
                )
            )
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
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
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
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
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
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
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
private fun GoalFormPreview() {
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
private fun ParameterListsPreview() {
    Card(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface).padding(16.dp)
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
