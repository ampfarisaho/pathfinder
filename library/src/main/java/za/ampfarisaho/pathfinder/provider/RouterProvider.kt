package za.ampfarisaho.pathfinder.provider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import za.ampfarisaho.pathfinder.Router

@Composable
fun RouterProvider(router: Router, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalRouter provides router) {
        content()
    }
}
