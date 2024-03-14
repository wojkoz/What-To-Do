package com.example.whattodo.presentation.tasks.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ValidUntilField(
    onClick: () -> Unit,
    label: String,
    value: String?,
    icon: ImageVector,
    iconDescription: String,
) {
    Box(
        modifier = Modifier
            .clickable { onClick.invoke() }
            .padding(vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, fontSize = 14.sp)
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            value?.let {
                Text(text = it, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.padding(horizontal = 10.dp))
            Icon(
                imageVector = icon,
                contentDescription = iconDescription,
            )
        }
    }
}