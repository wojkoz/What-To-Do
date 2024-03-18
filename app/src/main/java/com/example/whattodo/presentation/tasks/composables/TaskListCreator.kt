package com.example.whattodo.presentation.tasks.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.whattodo.R.string

@Composable
fun TaskListCreator(
    shouldForceSetActive: Boolean,
    onDismiss: () -> Unit,
    onConfirmation: (title: String, setActive: Boolean) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var setActive by remember { mutableStateOf(shouldForceSetActive) }
    val isTitleGood by remember(key1 = title) { mutableStateOf(title.length in 1..80) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = string.list_chooser_create_list),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = { Text(text = stringResource(id = string.title_req)) },
                    maxLines = 1,
                    modifier = Modifier.padding(top = 25.dp)
                )
                if (!isTitleGood) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(id = string.list_creator_label),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable { if (!shouldForceSetActive) setActive = !setActive }
                ) {
                    Checkbox(
                        checked = if (shouldForceSetActive) true else setActive,
                        onCheckedChange = { setActive = it }
                    )
                    Text(text = stringResource(id = string.set_active))
                }
                Button(
                    enabled = isTitleGood,
                    onClick = { onConfirmation(title, setActive) },
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.End)
                ) {
                    Text(text = stringResource(id = string.apply))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTaskListCreatorDialog() {
    TaskListCreator(
        onDismiss = {},
        onConfirmation = { _, _ -> },
        shouldForceSetActive = false,
    )
}