package za.ampfarisaho.pathfinder

/**
 * Base class for routers that handle navigation commands and result passing.
 *
 * Responsibilities:
 * - Stores a [NavigationCommandBuffer] for executing navigation commands.
 * - Manages result listeners and result delivery via [ResultRegistry].
 * - Provides method to execute commands.
 */
abstract class BaseRouter {

    internal val commandBuffer = NavigationCommandBuffer()
    private val resultWire = ResultRegistry()

    /**
     * Registers a listener that will be notified when a result is sent with the given [key].
     *
     * @param key unique identifier used to match results to listeners
     * @param listener callback that receives the result
     * @return a [ResultListenerHandler] that can be used to remove the listener
     */
    fun <R> setResultListener(
        key: String,
        listener: ResultListener<R>
    ): ResultListenerHandler {
        return resultWire.setResultListener(key, listener)
    }

    /**
     * Sends a result associated with a given [key] to the registered listener.
     *
     * @param key unique identifier for the result
     * @param data the result data
     */
    fun sendResult(key: String, data: Any?) {
        resultWire.sendResult(key, data)
    }

    /**
     * Executes one or more navigation [commands] by passing them
     * to the [NavigationCommandBuffer].
     */
    protected fun executeCommands(vararg commands: NavigationCommand) {
        commandBuffer.executeCommands(commands)
    }
}
