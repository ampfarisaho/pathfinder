package za.ampfarisaho.pathfinder

interface Navigator {
    fun executeCommands(vararg commands: NavigationCommand)
}
