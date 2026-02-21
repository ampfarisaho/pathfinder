package za.ampfarisaho.pathfinder.screens

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import za.ampfarisaho.pathfinder.navigation.Home
import za.ampfarisaho.pathfinder.navigation.PathOne
import za.ampfarisaho.pathfinder.provider.LocalRouter
import za.ampfarisaho.pathfinder.ui.components.ContentBase

@Composable
fun PathThreeScreen() {
    val router = LocalRouter.current
    ContentBase(
        title = "Screen 3",
        content = {
            Button(onClick = {
                router.replaceScreen(PathOne("ZA"))
            }) {
                Text(text = "Replace with Screen 1")
            }
            Button(onClick = {
                router.backToScreenByKey(Home.screenKey, inclusive = true)
            }) {
                Text(text = "Go to Home By Screen Key")
            }
            Button(onClick = {
                router.backByStep(3, inclusive = true)
            }) {
                Text(text = "Go to Root Screen By Step")
            }
            Button(onClick = { router.exit() }) {
                Text(text = "Go Back")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PathThreeScreenPreview() {
    MaterialTheme {
        PathThreeScreen()
    }
}
