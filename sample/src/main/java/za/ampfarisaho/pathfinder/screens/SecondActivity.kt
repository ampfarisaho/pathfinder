package za.ampfarisaho.pathfinder.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import za.ampfarisaho.pathfinder.ui.components.ContentBase

@AndroidEntryPoint
class SecondActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                ContentBase(
                    title = "Activity 2",
                    modifier = Modifier.safeContentPadding()
                ) {
                    Button(onClick = { onBackPressedDispatcher.onBackPressed() }) {
                        Text(text = "Go Back")
                    }
                }
            }
        }
    }
}