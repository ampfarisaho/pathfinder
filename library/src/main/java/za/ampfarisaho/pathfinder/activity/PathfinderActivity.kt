package za.ampfarisaho.pathfinder.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

/**
 * Base activity to handle Compose-friendly navigation with ActivityResultContracts.
 *
 * This class abstracts away the boilerplate of registering contracts and managing
 * callbacks for activity results. Subclasses just need to register contracts.
 */
abstract class PathfinderActivity : ComponentActivity() {

    private val launchers = mutableMapOf<String, ActivityResultLauncher<*>>()
    private val callbacks = mutableMapOf<String, (Any?) -> Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val registry = ActivityResultContractRegistry()
        registerContracts(registry)
        setupLaunchers(registry)
    }

    /**
     * Override this to register activity result contracts with unique keys.
     *
     * @param registry The registry to which contracts should be added.
     */
    protected abstract fun registerContracts(registry: ActivityResultContractRegistry)

    /**
     * Registers ActivityResultLaunchers for each contract.
     *
     * @param registry The registry containing all registered contracts.
     */
    private fun setupLaunchers(registry: ActivityResultContractRegistry) {
        for ((key, contract) in registry.contracts) {
            @Suppress("UNCHECKED_CAST")
            val launcher = registerForActivityResult(
                contract as ActivityResultContract<Any?, Any?>
            ) { result ->
                callbacks.remove(key)?.invoke(result)
            }
            launchers[key] = launcher
        }
    }

    /**
     * Launches any registered contract.
     *
     * @param key The unique key of the contract to launch.
     * @param input The input to pass to the contract.
     * @param callback The function to invoke with the contract result.
     *
     * @throws IllegalStateException if no launcher is registered with the given key.
     */
    @Suppress("UNCHECKED_CAST")
    fun <I, O> launch(
        key: String,
        input: I,
        callback: (O) -> Unit
    ) {
        callbacks[key] = callback as (Any?) -> Unit
        (launchers[key] as? ActivityResultLauncher<I>)
            ?.launch(input)
            ?: error("No launcher registered with key=$key")
    }
}
