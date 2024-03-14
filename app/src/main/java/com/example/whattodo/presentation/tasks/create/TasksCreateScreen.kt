package com.example.whattodo.presentation.tasks.create

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whattodo.R.string
import com.example.whattodo.domain.models.task.item.TaskPriority.High
import com.example.whattodo.domain.models.task.item.TaskPriority.Low
import com.example.whattodo.presentation.tasks.composables.CustomDatePickerDialog
import com.example.whattodo.presentation.tasks.composables.CustomTimePickerDialog
import com.example.whattodo.presentation.tasks.composables.ValidUntilField
import com.example.whattodo.presentation.tasks.create.model.TasksCreateEvent.OnContentChange
import com.example.whattodo.presentation.tasks.create.model.TasksCreateEvent.OnCreateTask
import com.example.whattodo.presentation.tasks.create.model.TasksCreateEvent.OnPriorityChange
import com.example.whattodo.presentation.tasks.create.model.TasksCreateEvent.OnTitleChange
import com.example.whattodo.presentation.tasks.create.model.TasksCreateUiEvent.NavigateBack
import com.example.whattodo.presentation.tasks.create.model.TasksCreateUiEvent.ShowMessage
import com.example.whattodo.presentation.tasks.create.model.TasksCreateEvent
import com.example.whattodo.presentation.tasks.create.model.TasksCreateState
import com.example.whattodo.presentation.tasks.create.model.TasksCreateUiEvent
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator
import com.example.whattodo.ui.composables.ErrorDialog
import com.example.whattodo.utils.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksCreateScreen(
    state: TasksCreateState,
    onEvent: (TasksCreateEvent) -> Unit,
    uiEvent: Flow<TasksCreateUiEvent>,
    onNavigateBack: () -> Unit,
) {
    val borderShapeRoundPercent = 20
    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val timeState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf<UiText?>(null) }
    val localContext = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        uiEvent.collect {
            when (it) {
                is ShowMessage -> showMessage = it.message
                NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = stringResource(
                    id = string.create_todo
                ),
                showBackIcon = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = {
                        onEvent(OnCreateTask)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(id = string.apply)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(borderShapeRoundPercent)
                    )
                    .padding(30.dp)
            ) {
                Text(
                    text = stringResource(id = string.details),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                    modifier = Modifier.padding(bottom = 15.dp)
                )
                OutlinedTextField(
                    value = state.title,
                    maxLines = 1,
                    label = { Text(text = stringResource(id = string.title_req)) },
                    onValueChange = {
                        onEvent(OnTitleChange(it))
                    },
                    isError = state.titleError != null,
                    supportingText = {
                        val titleError = state.titleError
                        if (titleError != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = titleError.asString(localContext),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = state.content,
                    label = { Text(text = stringResource(id = string.description)) },
                    onValueChange = {
                        onEvent(OnContentChange(it))
                    },
                    isError = state.contentError != null,
                    supportingText = {
                        val contentError = state.contentError
                        if (contentError != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = contentError.asString(localContext),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
            // date time picker min date today
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(borderShapeRoundPercent)
                    )
                    .padding(30.dp)
            ) {
                Text(
                    text = stringResource(id = string.valid_until),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                ValidUntilField(
                    onClick = { showDatePicker = true },
                    label = stringResource(id = string.date),
                    value = state.date,
                    icon = Icons.Filled.DateRange,
                    iconDescription = stringResource(id = string.edit_date),
                )
                Spacer(modifier = Modifier.height(10.dp))
                ValidUntilField(
                    onClick = { showTimePicker = true },
                    label = stringResource(id = string.time),
                    value = state.time,
                    icon = Icons.Filled.Create,
                    iconDescription = stringResource(id = string.edit_time),
                )
                if (showDatePicker) {
                    CustomDatePickerDialog(
                        onDismiss = { showDatePicker = false },
                        onConfirm = {
                            showDatePicker = false
                            onEvent(
                                TasksCreateEvent.OnDateTimeChange(
                                    date = dateState.selectedDateMillis,
                                    hour = timeState.hour,
                                    minute = timeState.minute,
                                )
                            )
                        },
                        datePickerState = dateState
                    )
                }
                if (showTimePicker) {
                    CustomTimePickerDialog(
                        onDismiss = { showTimePicker = false },
                        onConfirm = {
                            showTimePicker = false
                            onEvent(
                                TasksCreateEvent.OnDateTimeChange(
                                    date = dateState.selectedDateMillis,
                                    hour = timeState.hour,
                                    minute = timeState.minute,
                                )
                            )
                        },
                        timePickerState = timeState
                    )
                }
            }
            // priority radio button (low, high)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(borderShapeRoundPercent)
                    )
                    .padding(30.dp)
            ) {
                Text(
                    text = stringResource(id = string.priority),
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = Low == state.priority,
                        onClick = { onEvent(OnPriorityChange(Low)) }
                    )
                    Text(
                        text = stringResource(id = string.low),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = High == state.priority,
                        onClick = { onEvent(OnPriorityChange(High)) }
                    )
                    Text(
                        text = stringResource(id = string.high),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }

            if (state.isLoading) {
                CustomProgressIndicator()
            }

            if (showMessage != null) {
                ErrorDialog(
                    onDismiss = { showMessage = null },
                    onConfirm = { showMessage = null },
                    message = showMessage!!,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodosCreateScreen() {
    TasksCreateScreen(
        onNavigateBack = {},
        state = TasksCreateState().copy(date = "25.05.2024", time = "12:00"),
        onEvent = {},
        uiEvent = flowOf()
    )
}