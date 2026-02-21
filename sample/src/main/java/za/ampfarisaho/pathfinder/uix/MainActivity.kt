package za.ampfarisaho.pathfinder.uix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import za.ampfarisaho.pathfinder.PathFinderNavDisplay
import za.ampfarisaho.pathfinder.Pathfinder
import za.ampfarisaho.pathfinder.PathfinderNavigator
import za.ampfarisaho.pathfinder.Router
import za.ampfarisaho.pathfinder.navigation.Home
import za.ampfarisaho.pathfinder.navigation.Settings
import za.ampfarisaho.pathfinder.provider.RouterProvider
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var pathfinder: Pathfinder<Router>

    private val bottomNavScreens = listOf(Home, Settings)
    private val bottomNavScreenKeys = bottomNavScreens.map { it.screenKey }

    private val navigator: PathfinderNavigator by lazy {
        PathfinderNavigator(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val currentScreenKey by navigator.currentScreenKey.collectAsStateWithLifecycle("")
            MaterialTheme {
                Scaffold(
                    bottomBar = {
                        if (currentScreenKey in bottomNavScreenKeys) {
                            NavigationBar {
                                bottomNavScreens.forEach { screen ->
                                    NavigationBarItem(
                                        icon = { Icon(screen.icon, contentDescription = null) },
                                        label = { Text(screen.label) },
                                        selected = currentScreenKey == screen.screenKey,
                                        onClick = {
                                            if (currentScreenKey != screen.screenKey) {
                                                pathfinder.router.newScreenChain(screen)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    RouterProvider(router = pathfinder.router) {
                        PathFinderNavDisplay(
                            modifier = Modifier.padding(innerPadding),
                            navigator = navigator,
                            elements = arrayOf(Home)
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pathfinder.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        pathfinder.getNavigatorHolder().removeNavigator()
    }
}
