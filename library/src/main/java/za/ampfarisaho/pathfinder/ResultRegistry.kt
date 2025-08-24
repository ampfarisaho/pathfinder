package za.ampfarisaho.pathfinder

/**
 * Interface definition for a result callback.
 * This is used to receive a single result asynchronously.
 */
fun interface ResultListener<R> {
    fun onResult(data: R)
}

/**
 * Handler for manually disposing of a registered ResultListener.
 * Prevents memory leaks by allowing the listener to be removed when no longer needed.
 */
fun interface ResultListenerHandler {
    fun dispose()
}

/**
 * Registry that manages ResultListener instances keyed by a string.
 *
 * This allows clients to register a listener for a specific key and send a result
 * to it later. Once a result is sent or the handler is disposed, the listener is removed
 * from the registry to avoid memory leaks.
 */
internal class ResultRegistry {

    private val listeners = mutableMapOf<String, ResultListener<*>>()

    /**
     * Registers a ResultListener for a given key.
     *
     * @param key The unique key identifying this listener.
     * @param listener The listener to receive the result.
     * @return A ResultListenerHandler that can be used to dispose of the listener manually.
     */
    fun <R> setResultListener(key: String, listener: ResultListener<R>): ResultListenerHandler {
        listeners[key] = listener
        return ResultListenerHandler {
            listeners.remove(key)
        }
    }

    /**
     * Sends a result to the listener associated with the given key.
     * After sending, the listener is removed from the registry.
     *
     * @param key The key identifying which listener should receive the result.
     * @param data The result data to send.
     */
    fun <R> sendResult(key: String, data: R?) {
        val listener = listeners.remove(key) as? ResultListener<R>
        listener?.onResult(data as R)
    }
}
