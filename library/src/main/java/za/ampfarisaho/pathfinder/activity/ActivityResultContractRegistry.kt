package za.ampfarisaho.pathfinder.activity

import androidx.activity.result.contract.ActivityResultContract

/**
 * Registry to hold [ActivityResultContract] keyed by a unique string.
 */
class ActivityResultContractRegistry {

    internal val contracts = mutableMapOf<String, ActivityResultContract<*, *>>()

    /**
     * Registers an ActivityResultContract with a unique key.
     *
     * @param key The unique identifier for this contract.
     * @param contract The ActivityResultContract to register.
     */
    fun <I, O> register(
        key: String,
        contract: ActivityResultContract<I, O>
    ) {
        contracts[key] = contract
    }
}
