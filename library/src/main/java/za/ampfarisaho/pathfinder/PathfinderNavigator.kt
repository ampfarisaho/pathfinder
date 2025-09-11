package za.ampfarisaho.pathfinder

import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.ampfarisaho.pathfinder.content.ActivityScreen
import za.ampfarisaho.pathfinder.content.ComposeScreen
import za.ampfarisaho.pathfinder.content.Dialog
import za.ampfarisaho.pathfinder.content.Screen

class PathfinderNavigator(private val activity: ComponentActivity) : Navigator {

    private val _dialog = MutableStateFlow<Dialog?>(null)
    val dialog: StateFlow<Dialog?> = _dialog

    private var _backStack: NavBackStack<NavKey>? = null

    private val backStack: NavBackStack<NavKey>
        get() = _backStack
            ?: error("BackStack has not been initialized. Call setBackStack() first.")

    private val _currentScreenKey = MutableSharedFlow<String>()
    val currentScreenKey: Flow<String> = _currentScreenKey

    /**
     * Tracks the current top screen in the back stack and emits its screen key.
     */
    private fun updateCurrentScreenKey() {
        CoroutineScope(Dispatchers.Main).launch {
            (backStack.lastOrNull() as? ComposeScreen)?.let { lastScreen ->
                _currentScreenKey.emit(lastScreen.screenKey)
            }

            snapshotFlow { backStack.toList() }.collect { snapshot ->
                (snapshot.lastOrNull() as? ComposeScreen)?.let { lastScreen ->
                    _currentScreenKey.emit(lastScreen.screenKey)
                }
            }
        }
    }

    /**
     * Sets the navigation back stack that this navigator will operate on.
     *
     * @param backStack The back stack to manage screens.
     */
    fun setBackStack(backStack: NavBackStack<NavKey>) {
        this._backStack = backStack
        updateCurrentScreenKey()
    }

    /**
     * Executes one or more navigation commands sequentially.
     *
     * Supports navigation actions like push, pop, replace, clearing the stack,
     * showing/dismissing dialogs, and launching activity contracts.
     *
     * @param commands Vararg of NavigationCommand instances to execute.
     */
    override fun executeCommands(vararg commands: NavigationCommand) {
        for (command in commands) {
            when (command) {
                is NavigateTo -> executeNavigateTo(command)
                is Replace -> replace(command.screen)
                is BackToScreen -> backTo(command.screenKey, command.inclusive)
                is BackToScreenByKey -> backTo(command.screenKey, command.inclusive)
                is NewScreenChain -> clearAndSet(*command.screens.toTypedArray())
                is NewHomeScreenChain -> clearAndSet(*command.screens.toTypedArray())
                is Back -> pop()
                is ClearStack -> clearStack()
                is ShowDialog -> _dialog.value = command.dialog
                is DismissDialog -> _dialog.value = null
            }
        }
    }

    private fun executeNavigateTo(command: NavigateTo) {
        when (val screen = command.screen) {
            is ActivityScreen -> activity.startActivity(screen.createIntent(activity))
            is ComposeScreen -> push(screen)
        }
    }

    /**
     * Pushes a new screen onto the back stack.
     *
     * @param screen The screen to add on top of the stack.
     */
    private fun push(screen: Screen) {
        backStack.add(screen)
    }

    /**
     * Replaces the current top screen with a new screen.
     * If the back stack is empty, it behaves like push.
     *
     * @param screen The screen to replace the top with.
     */
    private fun replace(screen: Screen) {
        if (backStack.isNotEmpty()) {
            backStack.removeAt(backStack.size - 1)
        }
        backStack.add(screen)
    }

    /**
     * Removes the last screen from the back stack (pop).
     * Does nothing if the stack is empty.
     */
    private fun pop() {
        backStack.removeLastOrNull()
    }

    /**
     * Clears all screens from the back stack.
     */
    private fun clearStack() {
        backStack.clear()
    }

    /**
     * Navigates back to a specific screen identified by [screenKey].
     *
     * @param screenKey The key of the screen to navigate back to.
     * @param inclusive If true, the target screen is also removed; otherwise it remains.
     */
    private fun backTo(screenKey: String, inclusive: Boolean) {
        backStack.popBackStack(screenKey, inclusive)
    }

    private fun NavBackStack<NavKey>.popBackStack(screenKey: String, inclusive: Boolean) {
        val index = this
            .filterIsInstance<ComposeScreen>()
            .indexOfLast { it.screenKey == screenKey }

        if (index == -1) {
            throw IllegalArgumentException("Screen with key '$screenKey' not found in back stack")
        }

        val removeFrom = if (inclusive) index else index + 1

        val safeRemoveFrom = removeFrom.coerceAtMost(size - 1)
        while (size > safeRemoveFrom) {
            if (size == 1) break // Keep at least one screen
            removeAt(size - 1)
        }
    }

    /**
     * Clears the back stack and sets it to the provided screens.
     *
     * @param screens Vararg of screens to set as the new back stack.
     */
    private fun clearAndSet(vararg screens: Screen) {
        backStack.clear()
        backStack.addAll(screens)
    }
}
