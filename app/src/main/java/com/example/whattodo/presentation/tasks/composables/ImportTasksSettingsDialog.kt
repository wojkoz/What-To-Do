package com.example.whattodo.presentation.tasks.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.whattodo.R.string
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettings.ADD_AS_NEW
import com.example.whattodo.presentation.tasks.composables.ImportTasksSettings.CLEAR_DB_AND_ADD

@Composable
fun ImportTasksSettingsDialog(
    onDismiss: () -> Unit,
    onImport: (ImportTasksSettings) -> Unit,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(start = 8.dp, end = 8.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = string.choose_import_settings),
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp)
            )
            Button(
                onClick = { onImport.invoke(CLEAR_DB_AND_ADD) },
            ) {
                Text(text = stringResource(id = string.clear_db_and_add))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { onImport.invoke(ADD_AS_NEW) },
            ) {
                Text(text = stringResource(id = string.add_tasks_as_new))
            }
        }
    }
}

enum class ImportTasksSettings {
    CLEAR_DB_AND_ADD,
    ADD_AS_NEW,
}

@Preview(showBackground = true)
@Composable
fun PreviewImportTasksSettingsDialog() {
    ImportTasksSettingsDialog(
        onDismiss = {},
        onImport = {},
    )
}
