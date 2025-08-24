package za.ampfarisaho.pathfinder.provider

import androidx.compose.runtime.staticCompositionLocalOf
import za.ampfarisaho.pathfinder.Router

val LocalRouter = staticCompositionLocalOf<Router> {
    error("No Router provided. Make sure to wrap your composables in a RouterProvider.")
}
