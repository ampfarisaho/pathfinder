package za.ampfarisaho.pathfinder.screens

data class HomeButton(
    val text: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)
