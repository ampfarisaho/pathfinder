package za.ampfarisaho.pathfinder.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.serialization.Serializable
import za.ampfarisaho.pathfinder.content.ActivityScreen
import za.ampfarisaho.pathfinder.content.ComposeScreen
import za.ampfarisaho.pathfinder.screens.HomeScreen
import za.ampfarisaho.pathfinder.screens.PathOneScreen
import za.ampfarisaho.pathfinder.screens.PathOneViewModel
import za.ampfarisaho.pathfinder.screens.PathThreeScreen
import za.ampfarisaho.pathfinder.screens.PathTwoScreen
import za.ampfarisaho.pathfinder.screens.ResultPathScreen
import za.ampfarisaho.pathfinder.screens.SecondActivity
import za.ampfarisaho.pathfinder.screens.SettingsScreen

abstract class BottomNavScreen : ComposeScreen() {
    abstract val icon: ImageVector
    abstract val label: String
}

@Serializable
data object Home : BottomNavScreen() {

    override val icon: ImageVector get() = Icons.Default.Home
    override val label: String get() = "Home"

    @Composable
    override fun Content() = HomeScreen(hiltViewModel())
}

@Serializable
data object Settings : BottomNavScreen() {

    override val icon: ImageVector get() = Icons.Default.Settings
    override val label: String get() = "Settings"

    @Composable
    override fun Content() = SettingsScreen()
}

@Serializable
data class PathOne(val countryCode: String) : ComposeScreen() {
    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<PathOneViewModel, PathOneViewModel.Factory>(
            creationCallback = { factory -> factory.create(countryCode) }
        )
        PathOneScreen(viewModel)
    }
}

@Serializable
data object PathTwo : ComposeScreen() {
    @Composable
    override fun Content() = PathTwoScreen()
}

@Serializable
data object PathThree : ComposeScreen() {
    @Composable
    override fun Content() = PathThreeScreen()
}

@Serializable
data object ResultPath : ComposeScreen() {
    @Composable
    override fun Content() = ResultPathScreen()
}

data object ActivityTwo : ActivityScreen() {
    override fun createIntent(context: Context): Intent {
        return Intent(context, SecondActivity::class.java)
    }
}