package com.nabeelkm.workout.ui.screens

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nabeelkm.workout.entity.Goal
import com.nabeelkm.workout.entity.GoalStatus
import com.nabeelkm.workout.entity.Parameter
import com.nabeelkm.workout.entity.ParameterType
import com.nabeelkm.workout.navigation.AppNavigator
import com.nabeelkm.workout.navigation.AppRoute
import com.nabeelkm.workout.navigation.HomeRoute
import com.nabeelkm.workout.theme.AppColor
import com.nabeelkm.workout.theme.AppRadius
import com.nabeelkm.workout.theme.AppSpace
import com.nabeelkm.workout.theme.Theme
import com.nabeelkm.workout.ui.components.Card
import com.nabeelkm.workout.ui.components.PrimaryButton
import com.nabeelkm.workout.viewmodel.WorkoutFormState
import com.nabeelkm.workout.viewmodel.WorkoutFormViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import workout.shared.generated.resources.Res
import workout.shared.generated.resources.workout_icon
import kotlin.time.Clock

@Composable
fun WorkoutFormScreen(
    workoutId: Long? = null,
    initialGoalId: Int? = null,
    viewModel: WorkoutFormViewModel = koinViewModel(),
    navigator: AppNavigator,
) {
    val formState = viewModel.formStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        if (formState.value.time == null) {
            viewModel.onTimeUpdate(Clock.System.now().toEpochMilliseconds())
        }

        if (workoutId != null) {
            viewModel.setExistingWorkoutById(workoutId)
        }
        else if (initialGoalId != null) {
            viewModel.setGoalById(initialGoalId)
        }
    }

    LaunchedEffect(viewModel.uiChannelFlow) {
        viewModel.uiChannelFlow.collect { effect ->
            when(effect) {
                is WorkoutFormViewModel.UIEvent.SuccessAdd -> {
                    if (initialGoalId != null) {
                        navigator.navigate(AppRoute.DetailGoal(effect.workout.goalId))
                    } else {
                        navigator.navigate(AppRoute.Home(HomeRoute.Home))
                    }
                }
                else -> {}
            }
        }
    }

    val selectableGoals = viewModel.selectableGoalsFlow.collectAsStateWithLifecycle()

    WorkoutFormContent(
        formState = formState.value,
        selectableGoals = selectableGoals.value,
        selectedGoalParameters = emptyList(),
        onGoalSelected = viewModel::onGoalUpdate,
        onTimeChange = viewModel::onTimeUpdate,
        onDurationChange = { viewModel.onDuration(it.toLongOrNull()) },
        onNotesChange = { viewModel.onNotesUpdate(it) },
        onParameterValueChange = viewModel::onParameterValueUpdate,
        onSave = viewModel::onAddForm,
        onCancel = { navigator.goBack() },
    )
}

@Composable
private fun WorkoutFormContent(
    formState: WorkoutFormState,
    selectableGoals: List<Goal>,
    selectedGoalParameters: List<Parameter>,
    onGoalSelected: (Goal) -> Unit,
    onTimeChange: (Long) -> Unit,
    onDurationChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onParameterValueChange: (Int, String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    val showDateModal = remember { mutableStateOf(false) }
    val showTimeModal = remember { mutableStateOf(false) }
    val pendingDateMillis = remember { mutableStateOf<Long?>(null) }

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
                            "Log Workout",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Transparent,
                                contentColor = AppColor.fg2
                            ),
                            onClick = onCancel,
                            shape = RoundedCornerShape(AppRadius.md),
                            contentPadding = PaddingValues(12.dp, 8.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColor.bg
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = AppColor.border,
                        shape = RoundedCornerShape(0.dp)
                    )
                    .background(AppColor.bg)
                    .navigationBarsPadding()
                    .padding(AppSpace.s4),
                horizontalArrangement = Arrangement.spacedBy(AppSpace.s3)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onCancel,
                    shape = RoundedCornerShape(AppRadius.md),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColor.surface,
                        contentColor = AppColor.fg
                    ),
                    border = BorderStroke(1.dp, AppColor.border),
                    contentPadding = PaddingValues(18.dp, 10.dp)
                ) {
                    Text("Cancel")
                }
                PrimaryButton(
                    modifier = Modifier.weight(1f),
                    onClick = onSave,
                    contentPadding = PaddingValues(18.dp, 10.dp)
                ) {
                    Text("Save Workout")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpace.s4),
            verticalArrangement = Arrangement.spacedBy(AppSpace.s4)
        ) {
            // Goal selector
            FormField(label = "Goal") {
                if (selectableGoals.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(AppSpace.s2),
                        verticalArrangement = Arrangement.spacedBy(AppSpace.s2)
                    ) {
                        selectableGoals.forEach { goal ->
                            GoalPill(
                                goal = goal,
                                onClick = { onGoalSelected(goal) },
                                isSelected = formState.goal?.id == goal.id
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, AppColor.border, RoundedCornerShape(AppRadius.sm))
                            .background(AppColor.bg, RoundedCornerShape(AppRadius.sm))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            "No active goals available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColor.muted
                        )
                    }
                }
            }

            // Date + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpace.s3)
            ) {
                FormField(
                    label = "Date",
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDateModal.value = true }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = formState.time?.let { formatDateShort(it) } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("Select date") },
                            colors = disabledFieldColors(),
                            shape = RoundedCornerShape(AppRadius.sm),
                        )
                    }
                }
                FormField(
                    label = "Time",
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (formState.time != null) showTimeModal.value = true
                                else showDateModal.value = true
                            }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = formState.time?.let { formatTimeShort(it) } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                            placeholder = { Text("Select time") },
                            colors = disabledFieldColors(),
                            shape = RoundedCornerShape(AppRadius.sm),
                        )
                    }
                }
            }

            // Date Modal
            if (showDateModal.value) {
                DatePickerModal(
                    onDateSelected = { millis ->
                        if (millis != null) {
                            pendingDateMillis.value = millis
                            showTimeModal.value = true
                        }
                    },
                    onDismiss = { showDateModal.value = false }
                )
            }

            // Time Modal
            if (showTimeModal.value) {
                val tz = TimeZone.currentSystemDefault()
                val referenceMillis = formState.time ?: kotlin.time.Clock.System.now().toEpochMilliseconds()
                val referenceLocal = kotlin.time.Instant.fromEpochMilliseconds(referenceMillis).toLocalDateTime(tz)
                TimePickerModal(
                    initialHour = referenceLocal.hour,
                    initialMinute = referenceLocal.minute,
                    onTimeSelected = { hour, minute ->
                        val dateMillis = pendingDateMillis.value ?: formState.time
                        if (dateMillis != null) {
                            onTimeChange(combineDateAndTime(dateMillis, hour, minute))
                        }
                        pendingDateMillis.value = null
                    },
                    onDismiss = {
                        pendingDateMillis.value = null
                        showTimeModal.value = false
                    }
                )
            }

            // Duration
            FormField(label = "Duration (min)") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.duration?.toString() ?: "",
                    onValueChange = onDurationChange,
                    placeholder = { Text("30") },
                    colors = formFieldColors(),
                    shape = RoundedCornerShape(AppRadius.sm),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }

            // Metrics card (dynamic based on selected goal's parameters)
            if (selectedGoalParameters.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(AppSpace.s4),
                        verticalArrangement = Arrangement.spacedBy(AppSpace.s3)
                    ) {
                        Text(
                            "Metrics",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        selectedGoalParameters.forEach { param ->
                            FormField(
                                label = param.name,
                                hint = param.unit
                            ) {
                                val keyboardType = when (ParameterType.fromValue(param.type)) {
                                    ParameterType.STRING -> KeyboardType.Text
                                    ParameterType.INTEGER -> KeyboardType.Number
                                    ParameterType.FLOAT -> KeyboardType.Decimal
                                }
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = formState.parameterValues[param.id] ?: "",
                                    onValueChange = { onParameterValueChange(param.id, it) },
                                    placeholder = {
                                        Text(
                                            if (keyboardType == KeyboardType.Text) "" else "0"
                                        )
                                    },
                                    colors = formFieldColors(),
                                    shape = RoundedCornerShape(AppRadius.sm),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                                )
                            }
                        }
                    }
                }
            }

            // Notes
            FormField(label = "Notes") {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = formState.notes ?: "",
                    onValueChange = onNotesChange,
                    placeholder = { Text("How did it feel?") },
                    colors = formFieldColors(),
                    shape = RoundedCornerShape(AppRadius.sm),
                    minLines = 3,
                )
            }

            Spacer(Modifier.height(AppSpace.s4))
        }
    }
}

@Composable
private fun FormField(
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppSpace.s2)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = AppColor.fg2
                )
            )
            if (hint != null) {
                Text(
                    "· $hint",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppColor.muted
                )
            }
        }
        content()
    }
}

@Composable
fun GoalPill(goal: Goal, onClick: () -> Unit, isSelected: Boolean) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = AppSpace.s3, vertical = AppSpace.s2),
        shape = RoundedCornerShape(AppRadius.pill),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AppColor.accentSoft else AppColor.surface,
            contentColor = if (isSelected) AppColor.accent else AppColor.fg2,
        ),
        border = BorderStroke(
            1.dp,
            color = if (isSelected) AppColor.accent else AppColor.border
        )
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(Res.drawable.workout_icon),
            contentDescription = null,
            tint = AppColor.accent
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = goal.name,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun formFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AppColor.accent,
    unfocusedBorderColor = AppColor.border,
    focusedContainerColor = AppColor.bg,
    unfocusedContainerColor = AppColor.bg,
)

@Composable
private fun disabledFieldColors() = OutlinedTextFieldDefaults.colors(
    disabledBorderColor = AppColor.border,
    disabledTextColor = AppColor.fg,
    disabledContainerColor = AppColor.bg,
    disabledPlaceholderColor = AppColor.muted,
)

private fun combineDateAndTime(dateMidnightUtcMillis: Long, hour: Int, minute: Int): Long {
    val tz = TimeZone.currentSystemDefault()
    val utcDate = kotlin.time.Instant.fromEpochMilliseconds(dateMidnightUtcMillis)
        .toLocalDateTime(TimeZone.UTC)
    val localDateTime = LocalDateTime(utcDate.year, utcDate.monthNumber, utcDate.dayOfMonth, hour, minute, 0)
    return localDateTime.toInstant(tz).toEpochMilliseconds()
}

private fun formatDateShort(epochMillis: Long): String {
    return com.nabeelkm.workout.utils.formatDate(epochMillis)
}

private fun formatTimeShort(epochMillis: Long): String {
    val tz = kotlinx.datetime.TimeZone.currentSystemDefault()
    val dt = kotlin.time.Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(tz)
    return "${dt.hour.toString().padStart(2, '0')}:${dt.minute.toString().padStart(2, '0')}"
}

@Preview
@Composable
private fun WorkoutFormPreview() {
    val now = Clock.System.now().toEpochMilliseconds()
    Theme {
        WorkoutFormContent(
            formState = WorkoutFormState(
                time = now,
                notes = "",
            ),
            selectableGoals = listOf(
                Goal(1, "Half Marathon Prep", GoalStatus.ACTIVE.value, now, null, now),
                Goal(2, "Daily 5K", GoalStatus.ACTIVE.value, now, null, now),
                Goal(3, "Upper Body Strength", GoalStatus.ACTIVE.value, now, null, now),
            ),
            selectedGoalParameters = listOf(
                Parameter(11, 1, "Distance", ParameterType.FLOAT.value, "km"),
                Parameter(12, 1, "Pace", ParameterType.FLOAT.value, "min/km"),
                Parameter(13, 1, "Heart Rate", ParameterType.INTEGER.value, "bpm"),
            ),
            onGoalSelected = {},
            onTimeChange = {},
            onDurationChange = {},
            onNotesChange = {},
            onParameterValueChange = { _, _ -> },
            onSave = {},
            onCancel = {},
        )
    }
}

@Preview
@Composable
private fun WorkoutFormEmptyPreview() {
    Theme {
        WorkoutFormContent(
            formState = WorkoutFormState(),
            selectableGoals = emptyList(),
            selectedGoalParameters = emptyList(),
            onGoalSelected = {},
            onTimeChange = {},
            onDurationChange = {},
            onNotesChange = {},
            onParameterValueChange = { _, _ -> },
            onSave = {},
            onCancel = {},
        )
    }
}
