package com.example.whattodo.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize().clickable(enabled = false, onClick = {})) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .height(60.dp)
                .width(60.dp)
        )
    }
}