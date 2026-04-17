package za.ampfarisaho.pathfinder.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionScope
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
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent
import za.ampfarisaho.pathfinder.Back
import za.ampfarisaho.pathfinder.DismissDialog
import za.ampfarisaho.pathfinder.PathfinderNavigator
import za.ampfarisaho.pathfinder.content.ComposeScreen

private fun defaultEntryProvider(): (NavKey) -> NavEntry<NavKey> = { key ->
    NavEntry(key = key) { providedKey ->
        when (providedKey) {
            is ComposeScreen -> providedKey.Content()
            else -> error("Unknown nav entry key: $providedKey")
        }
    }
}

@Composable
fun PathFinderNavDisplay(
    modifier: Modifier = Modifier,
    navigator: PathfinderNavigator,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = { navigator.executeCommands(Back) },
    entryDecorators: List<NavEntryDecorator<NavKey>> =
        listOf(rememberSaveableStateHolderNavEntryDecorator()),
    sceneStrategies: List<SceneStrategy<NavKey>> =
        listOf(SinglePaneSceneStrategy()),
    sceneDecoratorStrategies: List<SceneDecoratorStrategy<NavKey>> = emptyList(),
    sharedTransitionScope: SharedTransitionScope? = null,
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultPopTransitionSpec(),
    predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(
        @NavigationEvent.SwipeEdge Int,
    ) -> ContentTransform = defaultPredictivePopTransitionSpec(),
    entryProvider: (NavKey) -> NavEntry<NavKey> = defaultEntryProvider(),
    elements: Array<out ComposeScreen>,
) {
    require(elements.isNotEmpty()) {
        "PathFinderNavDisplay requires at least one ComposeScreen in 'elements'."
    }

    val dialog by navigator.dialog.collectAsStateWithLifecycle()
    val backStack = rememberNavBackStack(*elements)

    DisposableEffect(backStack) {
        navigator.setBackStack(backStack)
        onDispose { /* No-op */ }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        contentAlignment = contentAlignment,
        onBack = onBack,
        entryDecorators = entryDecorators,
        sceneStrategies = sceneStrategies,
        sceneDecoratorStrategies = sceneDecoratorStrategies,
        sharedTransitionScope = sharedTransitionScope,
        sizeTransform = sizeTransform,
        transitionSpec = transitionSpec,
        popTransitionSpec = popTransitionSpec,
        predictivePopTransitionSpec = predictivePopTransitionSpec,
        entryProvider = entryProvider,
    )

    dialog?.Content {
        navigator.executeCommands(DismissDialog)
    }
}
