package za.ampfarisaho.pathfinder

import za.ampfarisaho.pathfinder.content.Dialog
import za.ampfarisaho.pathfinder.content.Screen

/**
 * Implementation of [BaseRouter] that interacts with [NavigationCommandBuffer].
 *
 * It forwards navigation commands to the navigator, manages dialogs, and handles
 * result listeners via [ResultRegistry].
 */
class Router : BaseRouter() {

    /**
     * Navigates to the given [screen] by pushing it on top of the back stack.
     */
    fun navigateTo(screen: Screen) {
        executeCommands(NavigateTo(screen))
    }

    /**
     * Replaces the current screen with the given [screen].
     * The old screen is removed from the back stack.
     */
    fun replaceScreen(screen: Screen) {
        executeCommands(Replace(screen))
    }

    /**
     * Navigates back to a specific [screen] in the back stack.
     *
     * @param screen the target screen to navigate back to
     * @param inclusive if true, also removes the target screen itself
     */
    fun backToScreen(screen: Screen, inclusive: Boolean) {
        executeCommands(BackToScreen(screen.screenKey, inclusive))
    }

    /**
     * Navigates back to a specific screen in the back stack by its [screenKey].
     *
     * @param screenKey the unique key of the target screen
     * @param inclusive if true, also removes the target screen itself
     */
    fun backToScreenByKey(screenKey: String, inclusive: Boolean) {
        executeCommands(BackToScreenByKey(screenKey, inclusive))
    }

    /**
     * Navigates back by a specified number of steps in the back stack.
     *
     * @param steps the number of screens to pop from the back stack
     * @param inclusive if true, also removes the target screen itself
     */
    fun backByStep(steps: Int, inclusive: Boolean) {
        executeCommands(BackByStep(steps, inclusive))
    }

    /**
     * Exits the current screen by popping it from the back stack.
     */
    fun exit() {
        executeCommands(Back)
    }

    /**
     * Replaces the entire back stack with a new chain of [screens].
     *
     * @param screens the new chain of screens
     */
    fun newScreenChain(vararg screens: Screen) {
        executeCommands(NewScreenChain(screens.toList()))
    }

    /**
     * Shows a [dialog] on top of the current screen.
     */
    fun showDialog(dialog: Dialog) {
        executeCommands(ShowDialog(dialog))
    }

    /**
     * Dismisses the currently visible dialog, if any.
     */
    fun dismissDialog() {
        executeCommands(DismissDialog)
    }
}

