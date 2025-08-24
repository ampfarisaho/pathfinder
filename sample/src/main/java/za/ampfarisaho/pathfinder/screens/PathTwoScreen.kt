package za.ampfarisaho.pathfinder.screens

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import za.ampfarisaho.pathfinder.navigation.PathOne
import za.ampfarisaho.pathfinder.navigation.PathThree
import za.ampfarisaho.pathfinder.navigation.PathTwo
import za.ampfarisaho.pathfinder.navigation.Settings
import za.ampfarisaho.pathfinder.provider.LocalRouter
import za.ampfarisaho.pathfinder.ui.components.ContentBase

@Composable
fun PathTwoScreen() {
    val router = LocalRouter.current
    ContentBase(
        title = "Screen 2",
        content = {
            Button(onClick = { router.navigateTo(PathThree) }) {
                Text(text = "Go to Screen 3")
            }
            Button(onClick = { router.newScreenChain(Settings) }) {
                Text(text = "New Screen Chain (Settings)")
            }
            Button(onClick = {
                router.newScreenChain(
                    Settings,
                    PathOne("ZA"),
                    PathTwo,
                    PathThree
                )
            }) {
                Text(text = "New Multi Screen Chain (Settings)")
            }
            Button(onClick = { router.exit() }) {
                Text(text = "Go Back")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun PathTwoScreenPreview() {
    MaterialTheme {
        PathTwoScreen()
    }
}
