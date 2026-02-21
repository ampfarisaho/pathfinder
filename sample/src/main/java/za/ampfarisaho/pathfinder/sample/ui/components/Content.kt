package za.ampfarisaho.pathfinder.sample.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// https://github.com/android/nav3-recipes/blob/main/app/src/main/java/com/example/nav3recipes/content/Content.kt
@Composable
fun ContentBase(
    title: String,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Title(title)
        content()
    }
}

@Composable
fun ColumnScope.Title(title: String) {
    Text(
        modifier = Modifier
            .padding(24.dp)
            .align(Alignment.CenterHorizontally),
        fontWeight = FontWeight.Bold,
        text = title
    )
}
