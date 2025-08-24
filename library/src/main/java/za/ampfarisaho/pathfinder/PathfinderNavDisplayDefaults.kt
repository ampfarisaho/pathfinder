package za.ampfarisaho.pathfinder

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.SceneStrategy
import androidx.navigation3.ui.SinglePaneSceneStrategy
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import androidx.navigationevent.NavigationEvent.SwipeEdge
import za.ampfarisaho.pathfinder.content.ComposeScreen

object PathfinderNavDisplayDefaults {

    val contentAlignment: Alignment = Alignment.TopStart

    @Composable
    fun entryDecorators(): List<NavEntryDecorator<*>> = listOf(
        rememberSceneSetupNavEntryDecorator(),
        rememberSavedStateNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )

    val entryProvider: (key: NavKey) -> NavEntry<NavKey> = { key ->
        NavEntry(key = key) {
            (key as ComposeScreen).Content()
        }
    }

    val sceneStrategy: SceneStrategy<NavKey> = SinglePaneSceneStrategy()

    val transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        ContentTransform(
            fadeIn(animationSpec = tween(700)),
            fadeOut(animationSpec = tween(700))
        )
    }

    val popTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform = {
        ContentTransform(
            fadeIn(animationSpec = tween(700)),
            fadeOut(animationSpec = tween(700))
        )
    }

    val predictivePopTransitionSpec: AnimatedContentTransitionScope<*>.(@SwipeEdge Int) -> ContentTransform =
        NavDisplay.defaultPredictivePopTransitionSpec
}
