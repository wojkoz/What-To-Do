package com.example.whattodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.whattodo.presentation.tasks.create.TasksCreateScreen
import com.example.whattodo.presentation.tasks.create.TasksCreateViewModel
import com.example.whattodo.presentation.tasks.list.TasksScreen
import com.example.whattodo.presentation.tasks.list.TasksViewModel
import com.example.whattodo.ui.theme.WhatToDoTheme
import com.example.whattodo.utils.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatToDoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Route.MAIN_SCREEN) {
                        composable(Route.MAIN_SCREEN) {
                            val viewModel: TasksViewModel = hiltViewModel()
                            val state by viewModel.uiState.collectAsStateWithLifecycle()
                            TasksScreen(
                                state = state,
                                onEvent = { event ->
                                    viewModel.onEvent(event)
                                },
                                onNavigateToCreateTask = { parentListId, taskItemId ->
                                    navController.navigate(
                                        route = Route.passArgToTodosCreateScreen(
                                            parentListId = parentListId,
                                            taskId = taskItemId
                                        )
                                    )
                                },
                                onUiEvent = viewModel.uiEvent
                            )
                        }
                        composable(Route.TODOS_CREATE_SCREEN) {
                            val viewModel: TasksCreateViewModel = hiltViewModel()
                            val state by viewModel.uiState.collectAsStateWithLifecycle()
                            TasksCreateScreen(
                                state = state,
                                onEvent = { event ->
                                    viewModel.onEvent(event)
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                uiEvent = viewModel.uiEvent
                            )
                        }
                    }
                }
            }
        }
    }
}
