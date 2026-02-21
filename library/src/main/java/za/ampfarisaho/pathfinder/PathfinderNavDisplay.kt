package za.ampfarisaho.pathfinder

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent
import za.ampfarisaho.pathfinder.content.ComposeScreen

@Composable
fun PathFinderNavDisplay(
    modifier: Modifier = Modifier,
    navigator: PathfinderNavigator,
    contentAlignment: Alignment = PathfinderNavDisplayDefaults.contentAlignment,
    onBack: () -> Unit = { navigator.executeCommands(Back) },
    entryDecorators: List<NavEntryDecorator<NavKey>> = PathfinderNavDisplayDefaults.entryDecorators(),
    sceneStrategy: SceneStrategy<NavKey> = PathfinderNavDisplayDefaults.sceneStrategy,
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform =
        PathfinderNavDisplayDefaults.transitionSpec,
    popTransitionSpec: AnimatedContentTransitionScope<*>.() -> ContentTransform =
        PathfinderNavDisplayDefaults.popTransitionSpec,
    predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(
        @NavigationEvent.SwipeEdge Int,
    ) -> ContentTransform = PathfinderNavDisplayDefaults.predictivePopTransitionSpec(),
    entryProvider: (key: NavKey) -> NavEntry<NavKey> = PathfinderNavDisplayDefaults.entryProvider,
    elements: Array<out ComposeScreen>,
) {
    if (elements.isEmpty()) {
        throw IllegalStateException(
            "AppNavigator requires at least one ComposeScreen in the 'elements' vararg. " +
                    "Provide one or more ComposeScreen instances to properly initialize the navigator."
        )
    }

    val dialog by navigator.dialog.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(*elements)

    DisposableEffect(backStack) {
        navigator.setBackStack(backStack)
        onDispose {}
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        contentAlignment = contentAlignment,
        onBack = onBack,
        entryDecorators = entryDecorators,
        sceneStrategy = sceneStrategy,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider
    )

    dialog?.let {
        it.Content {
            navigator.executeCommands(DismissDialog)
        }
    }
}
