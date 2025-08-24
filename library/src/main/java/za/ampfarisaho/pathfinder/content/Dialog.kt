package za.ampfarisaho.pathfinder.content

import androidx.compose.runtime.Composable

abstract class Dialog {
    @Composable
    abstract fun Content(onDismissRequest: () -> Unit)
}
