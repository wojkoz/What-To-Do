import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.whattodo.R
import com.example.whattodo.ui.composables.AppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen() {
    Scaffold(
        topBar = { AppBar(title = stringResource(id = R.string.main_screen_title), showBackIcon = false) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                // list chooser
            }

            item {
                // to do listView
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    TodosScreen()
}
