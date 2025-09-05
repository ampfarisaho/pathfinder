package za.ampfarisaho.pathfinder

import android.os.Handler
import android.os.Looper

/**
 * [NavigationCommandBuffer] manages the reference to the current [PathfinderNavigator] and provides
 * a safe way to execute navigation commands even when the navigator is temporarily unavailable.
 *
 * This class helps prevent memory leaks by holding a weak reference to the navigator and buffering
 * any navigation commands issued while the navigator is detached. Once a navigator is attached,
 * all pending commands are executed in order.
 */
class NavigationCommandBuffer : NavigatorHolder {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val pendingCommands = mutableListOf<Array<out NavigationCommand>>()
    private var navigator: Navigator? = null

    /**
     * Attaches a new navigator and executes any commands that were queued while no navigator was attached.
     *
     * This ensures that navigation actions issued before the navigator was available are not lost.
     *
     * @param navigator The navigator to attach for immediate and future command execution.
     */
    override fun setNavigator(navigator: Navigator) {
        this.navigator = navigator
        pendingCommands.forEach(navigator::executeCommands)
        pendingCommands.clear()
    }

    override fun removeNavigator() {
        navigator = null
    }

    /**
     * Executes navigation commands through the currently attached navigator.
     *
     * If the navigator is attached, commands are executed immediately.
     * Otherwise, commands are added to the pending queue to be executed
     * automatically when a navigator is attached via [setNavigator].
     *
     * @param commands Vararg of NavigationCommand instances to execute.
     */
    fun executeCommands(commands: Array<out NavigationCommand>) {
        mainHandler.post {
            navigator?.executeCommands(*commands) ?: pendingCommands.add(commands)
        }
    }
}
