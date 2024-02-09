import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.whattodo.R
import com.example.whattodo.presentation.todos.TodosEvent.OnTaskListSelect
import com.example.whattodo.presentation.todos.TodosViewModel
import com.example.whattodo.presentation.todos.composables.ListChooser
import com.example.whattodo.ui.composables.AppBar
import com.example.whattodo.ui.composables.CustomProgressIndicator

@Composable
fun TodosScreen(
    viewModel: TodosViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.main_screen_title), showBackIcon = false) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                item {
                    ListChooser(
                        currentList = state.activeTaskList,
                        options = state.taskLists,
                        onListSelect = { viewModel.onEvent(OnTaskListSelect(it)) },
                    )
                }

                item {
                    // to do listView
                }
            }
            if (state.isLoading) {
                CustomProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    TodosScreen()
}
