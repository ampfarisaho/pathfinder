package za.ampfarisaho.pathfinder.sample.screens

data class HomeButton(
    val text: String,
    val enabled: Boolean = true,
    val onClick: () -> Unit
)
