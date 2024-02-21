package com.example.whattodo.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.whattodo.R.string
import com.example.whattodo.utils.UiText

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    message: UiText,
) {
    val localContext = LocalContext.current
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message.asString(localContext),
                fontSize = 16.sp,
                modifier = Modifier.padding(10.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { onDismiss.invoke() }) {
                    Text(text = stringResource(id = string.cancel))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { onConfirm.invoke() },
                ) {
                    Text(text = stringResource(id = string.ok))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(
        onDismiss = {},
        onConfirm = {},
        message = UiText.StringResource(string.oops_something_went_wrong)
    )
}