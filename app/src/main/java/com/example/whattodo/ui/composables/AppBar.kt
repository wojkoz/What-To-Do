package com.example.whattodo.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.whattodo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    showBackIcon: Boolean,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit) = {},
    onNavigateBack: (() -> Unit) = {},
) {
    Box(modifier = modifier)
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        actions = actions,
        navigationIcon = {
            if(showBackIcon) {
                IconButton(onClick = { onNavigateBack.invoke() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_back)
                    )
                }
            }
        }
    )
}