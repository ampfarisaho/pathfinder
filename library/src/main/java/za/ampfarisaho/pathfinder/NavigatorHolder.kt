package za.ampfarisaho.pathfinder

interface NavigatorHolder {
    /**
     * Attaches a new navigator.
     *
     * @param navigator The navigator to attach for immediate and future command execution.
     */
    fun setNavigator(navigator: Navigator)

    /**
     * Detaches the current navigator reference.
     *
     * This is typically called when the associated Activity is stopping,
     * preventing memory leaks by removing the reference to the navigator.
     */
    fun removeNavigator()
}
