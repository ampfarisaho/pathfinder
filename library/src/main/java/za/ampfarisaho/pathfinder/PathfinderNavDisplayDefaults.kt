package za.ampfarisaho.pathfinder

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigationevent.NavigationEvent
import za.ampfarisaho.pathfinder.content.ComposeScreen

object PathfinderNavDisplayDefaults {

    val contentAlignment: Alignment = Alignment.TopStart

    @Composable
    fun entryDecorators(): List<NavEntryDecorator<NavKey>> = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )

    val entryProvider: (key: NavKey) -> NavEntry<NavKey> = { key ->
        NavEntry(key = key) { providedKey ->
            when (providedKey) {
                is ComposeScreen -> providedKey.Content()
                else -> error("Unknown key: $key")
            }
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

    fun predictivePopTransitionSpec(): AnimatedContentTransitionScope<Scene<NavKey>>.(
        @NavigationEvent.SwipeEdge Int,
    ) -> ContentTransform = {
        ContentTransform(
            fadeIn(
                spring(
                    dampingRatio = 1.0f, // reflects material3 motionScheme.defaultEffectsSpec()
                    stiffness = 1600.0f, // reflects material3 motionScheme.defaultEffectsSpec()
                )
            ),
            scaleOut(targetScale = 0.7f),
        )
    }
}
