package za.ampfarisaho.pathfinder

/**
 * Pathfinder is a helper class that wraps a router of type [BaseRouter]
 * and provides navigation-related functionality.
 */
class Pathfinder<T : BaseRouter> private constructor(val router: T) {

    /**
     * Exposes the [NavigatorHolder] from the router's command buffer
     * [NavigatorHolder] is typically used to attach/detach a navigator.
     */
    fun getNavigatorHolder(): NavigatorHolder = router.commandBuffer

    companion object {
        /**
         * Creates a [Pathfinder] instance with the default [BaseRouter].
         *
         * The default router is an anonymous subclass of [BaseRouter].
         */
        @JvmStatic
        fun create() = create(object : BaseRouter() {})

        /**
         * Creates a [Pathfinder] instance with a custom router.
         * This allows clients to pass in their own navigation logic.
         *
         * @param customRouter a router that extends [BaseRouter]
         */
        @JvmStatic
        fun <T : BaseRouter> create(customRouter: T) = Pathfinder(customRouter)
    }
}
