package com.example.whattodo.presentation.todos.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.domain.models.task.item.TaskPriority
import com.example.whattodo.domain.models.task.item.TaskPriority.High
import com.example.whattodo.domain.models.task.item.TaskPriority.Low
import java.time.LocalDateTime

@Composable
fun TaskItemView(
    item: TaskItem,
    onCheckChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isChecked by remember { mutableStateOf(item.isDone) }
    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(color = getColorPriorityForTaskItem(item.priority))
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .height(80.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    maxLines = 1,
                )
                Text(
                    text = item.content,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    // isChecked = it
                    onCheckChange()
                },
                modifier = Modifier
                    .size(30.dp)
                    .padding(horizontal = 20.dp)
            )
        }
    }
}

private fun getColorPriorityForTaskItem(priority: TaskPriority): Color {
    return when (priority) {
        High -> Color.Red
        Low -> Color.Magenta
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskItemValidHighPriorityTodo() {
    TaskItemView(
        item = TaskItem(
            id = 0,
            title = "Title",
            isValid = true,
            content = "extra long content but long enought but still long",
            createdAt = LocalDateTime.now(),
            isDone = false,
            parentListId = 0,
            priority = High,
            validUntil = LocalDateTime.now().plusDays(1)
        ),
        onCheckChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskItemNotValidLowPriorityTodo() {
    TaskItemView(
        item = TaskItem(
            id = 0,
            title = "Title",
            isValid = false,
            content = "extra long content but long enought but still long",
            createdAt = LocalDateTime.now(),
            isDone = false,
            parentListId = 0,
            priority = Low,
            validUntil = LocalDateTime.now().minusDays(1)
        ),
        onCheckChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskItemNotValidHighPriorityTodo() {
    TaskItemView(
        item = TaskItem(
            id = 0,
            title = "Title",
            isValid = false,
            content = "extra long content but long enought but still long",
            createdAt = LocalDateTime.now(),
            isDone = false,
            parentListId = 0,
            priority = High,
            validUntil = LocalDateTime.now().minusDays(1)
        ),
        onCheckChange = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskItemLowPriorityDone() {
    TaskItemView(
        item = TaskItem(
            id = 0,
            title = "Title",
            isValid = false,
            content = "extra long content but long enought but still long",
            createdAt = LocalDateTime.now(),
            isDone = true,
            parentListId = 0,
            priority = Low,
            validUntil = LocalDateTime.now().minusDays(1)
        ),
        onCheckChange = {}
    )
}

