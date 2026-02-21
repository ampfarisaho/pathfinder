package za.ampfarisaho.pathfinder.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import za.ampfarisaho.pathfinder.content.Dialog

data class WarningDialog(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val positiveButtonClick: (() -> Unit)? = null,
    val negativeButtonText: String,
    val negativeButtonClick: (() -> Unit)? = null
) : Dialog() {
    @Composable
    override fun Content(onDismissRequest: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = {
                    onDismissRequest()
                    positiveButtonClick?.invoke()
                }) {
                    Text(positiveButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismissRequest()
                    negativeButtonClick?.invoke()
                }) {
                    Text(negativeButtonText)
                }
            }
        )
    }
}

data class BottomSheet(val message: String) : Dialog() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(onDismissRequest: () -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
            ) {
                Text(message)
            }
        }
    }
}