package za.ampfarisaho.pathfinder.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import za.ampfarisaho.pathfinder.provider.LocalRouter
import za.ampfarisaho.pathfinder.ui.components.ContentBase

@Composable
fun ResultPathScreen() {
    val router = LocalRouter.current
    ContentBase(
        title = "Result Screen",
        content = {
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                router.sendResult(
                    key = "result_message",
                    data = "This is a result from another screen"
                )
                router.exit()
            }) {
                Text(text = "Send Result & Go Back")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ResultPathScreenPreview() {
    MaterialTheme {
        ResultPathScreen()
    }
}