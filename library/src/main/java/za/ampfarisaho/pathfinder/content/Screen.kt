package za.ampfarisaho.pathfinder.content

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
abstract class Screen : NavKey {
    open val screenKey: String
        get() = javaClass.name
}

@Serializable
abstract class ComposeScreen : Screen() {
    @Composable
    abstract fun Content()
}

@Serializable
abstract class ActivityScreen : Screen() {
    abstract fun createIntent(context: Context): Intent
}
