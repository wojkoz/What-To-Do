package com.example.whattodo.presentation.todos.create

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whattodo.R.string
import com.example.whattodo.domain.models.task.item.TaskPriority.High
import com.example.whattodo.domain.models.task.item.TaskPriority.Low
import com.example.whattodo.presentation.todos.composables.CustomDatePickerDialog
import com.example.whattodo.presentation.todos.composables.CustomTimePickerDialog
import com.example.whattodo.presentation.todos.composables.ValidUntilField
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnContentChange
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnCreateTask
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnPriorityChange
import com.example.whattodo.presentation.todos.create.TodosCreateEvent.OnTitleChange
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosCreateScreen(
    state: TodosCreateState,
    onEvent: (TodosCreateEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val borderShapeRoundPercent = 20
    val dateState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
    val timeState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

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
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
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

                // title tf max 80 chars
                OutlinedTextField(
                    value = state.title,
                    maxLines = 2,
                    label = { Text(text = stringResource(id = string.title_req)) },
                    onValueChange = {
                        onEvent(OnTitleChange(it))
                    },
                )
                Spacer(modifier = Modifier.height(20.dp))
                // content tf max 250 chars
                OutlinedTextField(
                    value = state.content,
                    label = { Text(text = stringResource(id = string.description)) },
                    onValueChange = {
                        onEvent(OnContentChange(it))
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
                                TodosCreateEvent.OnDateTimeChange(
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
                                TodosCreateEvent.OnDateTimeChange(
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodosCreateScreen() {
    TodosCreateScreen(
        onNavigateBack = {},
        state = TodosCreateState().copy(date = "25.05.2024", time = "12:00"),
        onEvent = {}
    )
}