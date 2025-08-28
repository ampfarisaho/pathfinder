package za.ampfarisaho.pathfinder

import za.ampfarisaho.pathfinder.content.Dialog
import za.ampfarisaho.pathfinder.content.Screen

interface NavigationCommand

/**
 * Navigate to a new screen and add it on top of the current navigation stack
 */
data class NavigateTo(val screen: Screen) : NavigationCommand

/**
 * Replace the current screen with a new one (does not keep the current screen in the stack)
 */
data class Replace(val screen: Screen) : NavigationCommand

/**
 * Go back to a specific screen in the stack identified by screenKey
 * If inclusive is true, also remove the target screen itself
 */
data class BackToScreen(val screenKey: String, val inclusive: Boolean) : NavigationCommand

/**
 * Alternative to BackToScreen, also goes back to a specific screen by key
 */
data class BackToScreenByKey(val screenKey: String, val inclusive: Boolean) : NavigationCommand

/**
 * Replace the current stack with a new chain of screens
 */
data class NewScreenChain(val screens: List<Screen>) : NavigationCommand

/**
 * Replace the current stack with a new chain of
 * screens and mark the first screen as the root screen
 */
data class NewHomeScreenChain(val screens: List<Screen>) : NavigationCommand

/**
 *  Navigate back one step in the stack (pop the current screen)
 */
object Back : NavigationCommand

/**
 * Clear the entire navigation stack (no screens left in the stack)
 */
object ClearStack : NavigationCommand

/**
 * Show a dialog on top of the current screen
 */
data class ShowDialog(val dialog: Dialog) : NavigationCommand

/**
 * Dismiss the currently visible dialog
 */
object DismissDialog : NavigationCommand
