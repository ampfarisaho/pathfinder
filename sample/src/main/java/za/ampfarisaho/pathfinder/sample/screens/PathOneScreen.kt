package za.ampfarisaho.pathfinder.sample.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import za.ampfarisaho.pathfinder.sample.ui.components.ContentBase

@Composable
private fun PathOneScreen(countryName: String, onBack: () -> Unit, onNext: () -> Unit) {
    ContentBase(
        title = "Screen 1",
        content = {
            Text(
                text = "$countryName was retrieved using the provided country code.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onNext) {
                Text(text = "Go to Screen 2")
            }
            Button(onClick = onBack) {
                Text(text = "Go Back")
            }
        }
    )
}

@Composable
fun PathOneScreen(viewModel: PathOneViewModel) {
    val countryName by viewModel.countryName.collectAsStateWithLifecycle()
    PathOneScreen(
        countryName = countryName,
        onBack = viewModel::goBack,
        onNext = viewModel::goToPathTwo
    )
}

@Preview(showBackground = true)
@Composable
private fun PathOneScreenPreview() {
    MaterialTheme {
        PathOneScreen(countryName = "South Africa", onBack = {}, onNext = {})
    }
}
