package com.example.whattodo.presentation.tasks.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import java.time.LocalDateTime

@Composable
fun TaskListView(
    title: String,
    items: List<TaskItem>,
    onAddTaskClick: (taskId: Long?) -> Unit,
    onTaskDone: (taskItem: TaskItem) -> Unit,
    modifier: Modifier = Modifier,
    showAddButton: Boolean = true,
) {

    var isExtended by remember { mutableStateOf(true) }

    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(percent = 10)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { isExtended = !isExtended }
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(vertical = 6.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium
                )
                if (isExtended) {
                    Image(
                        imageVector = Filled.KeyboardArrowDown,
                        contentDescription = "extended",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                } else {
                    Image(
                        imageVector = Filled.KeyboardArrowUp,
                        contentDescription = "not extended",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }


            AnimatedVisibility(visible = isExtended) {
                Column {
                    items.forEach { taskItem ->
                        Row(modifier = Modifier.clickable { onAddTaskClick(taskItem.id) }) {
                            TaskItemView(
                                item = taskItem,
                                onCheckChange = {
                                    onTaskDone(taskItem)
                                }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                    if (showAddButton) {
                        Box(
                            contentAlignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            FilledIconButton(onClick = {
                                onAddTaskClick(null)
                            }) {
                                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Task")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskListView() {
    TaskListView(
        title = "Todo",
        onAddTaskClick = {},
        onTaskDone = {},
        items = listOf(
            TaskItem(
                id = 0,
                title = "Title",
                isValid = true,
                content = "extra long content but long enought but still long",
                createdAt = LocalDateTime.now(),
                isDone = false,
                parentListId = 0,
                priority = TaskPriority.High,
                validUntil = LocalDateTime.now().plusDays(1)
            ),
            TaskItem(
                id = 1,
                title = "Title",
                isValid = true,
                content = "extra long content but long enought but still long",
                createdAt = LocalDateTime.now(),
                isDone = false,
                parentListId = 0,
                priority = TaskPriority.Low,
                validUntil = LocalDateTime.now().plusDays(1)
            )
        )
    )
}