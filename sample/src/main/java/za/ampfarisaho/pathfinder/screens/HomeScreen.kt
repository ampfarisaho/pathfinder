package za.ampfarisaho.pathfinder.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import za.ampfarisaho.pathfinder.ui.components.ContentBase

@Composable
private fun HomeScreen(buttons: List<HomeButton>) {
    ContentBase(
        title = "Welcome to Pathfinder",
        content = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(horizontal = 48.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items = buttons) { button ->
                    Button(
                        enabled = button.enabled,
                        onClick = button.onClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = button.text)
                    }
                }
            }
        }
    )
}

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val buttons = listOf(
        HomeButton(text = "Navigate Screens", onClick = viewModel::navigateToPathOne),
        HomeButton(text = "Open Result Screen", onClick = viewModel::navigateToResultPath),
        HomeButton(text = "Open Activity 2", onClick = viewModel::navigateToActivityTwo),
        HomeButton(text = "Show Dialog", onClick = viewModel::showDialog),
        HomeButton(text = "Show Bottom Sheet", onClick = viewModel::showBottomSheet)
    )
    HomeScreen(buttons = buttons)
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val buttons = List(5) {
        HomeButton(text = "Button $it", onClick = {})
    }
    MaterialTheme {
        HomeScreen(buttons = buttons)
    }
}
