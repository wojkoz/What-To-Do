package com.example.whattodo.presentation.todos.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.whattodo.domain.models.TaskList

@Composable
fun ListChooser(
    currentList: TaskList,
    onListSelect: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .clickable { onListSelect.invoke() }
                .padding(all = 4.dp)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentList.title,
            )
        }
    }
}