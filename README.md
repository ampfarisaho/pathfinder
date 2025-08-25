## What is it?

Pathfinder is a lightweight Jetpack Compose navigation library built on Navigation 3. It simplifies navigation in Android apps by providing a clear, type-safe, and extensible API. Pathfinder handles screen navigation, dialogs, activity contracts, and back stack management.

## Features

* Type-safe navigation using `ComposeScreen`, `ActivityScreen`, and `Dialog`.
* Simple back stack management.
* Safe execution of navigation commands even when the activity or navigator is temporarily unavailable.
* Support for activity result contracts via `PathfinderActivity`.
* Works with or without dependency injection frameworks like Hilt.
* Compose integration with `RouterProvider` and `PathFinderNavDisplay`.

## Setup

### 1. Add JitPack Repository

In your project `settings.gradle` or `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. Project `build.gradle` (or `build.gradle.kts`)

Add the Kotlin serialization plugin:

```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "<kotlin_version>" apply false
}
```

> Replace `<kotlin_version>` with the version matching your project’s Kotlin version.

### 3. App module `build.gradle` (or `build.gradle.kts`)

Apply the plugin and add dependencies:

```kotlin
plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

dependencies {
    implementation("com.github.ampfarisaho:pathfinder:1.0.3")
    implementation("androidx.navigation3:navigation3-ui:1.0.0-alpha07")
}
```

## Basic Usage (Without Dependency Injection)

### Initialize Pathfinder

Inside your `PathfinderActivity` (e.g. `MainActivity`):

```kotlin
class MainActivity : PathfinderActivity() {

    private val pathfinder: Pathfinder = Pathfinder.create(Router())

    private val navigator: PathfinderNavigator by lazy {
        PathfinderNavigator(this)
    }
}
```

### Set Content and Navigation

Wrap your `PathFinderNavDisplay` with `RouterProvider` to allow safe router access from composables:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
        MaterialTheme {
            RouterProvider(router = pathfinder.router) {
                PathFinderNavDisplay(
                    navigator = navigator,
                    elements = arrayOf(Home)
                )
            }
        }
    }
}
```

### Attach and Detach Navigator

Attach the navigator in `onResume` to execute navigation commands when the app is visible:

```kotlin
override fun onResume() {
    super.onResume()
    pathfinder.getNavigatorHolder().setNavigator(navigator)
}
```

Detach the navigator in `onPause` to prevent navigation commands from executing when the app is not visible:

```kotlin
override fun onPause() {
    super.onPause()
    pathfinder.getNavigatorHolder().removeNavigator()
}
```

> **Important:** Any navigation commands issued while the navigator is detached (i.e. between `onPause` and `onResume`) are queued and will automatically execute in order when the navigator is re-attached during `onResume`. This ensures that no navigation operations are lost even if they occur while the app is not visible.
> 

### Define Screens

`Screens.kt`:

```kotlin
object AppInfo : ActivityScreen() {
    override fun createIntent(context: Context) = 
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:$packageName".toUri()
        }
}

@Serializable
object Home : ComposeScreen() {
    @Composable
    override fun Content() = HomeScreen()
}

@Serializable
object Products : ComposeScreen() {
    @Composable
    override fun Content() = ProductListScreen()
}
```

### Define Dialogs

`Dialogs.kt`:

```kotlin
data class BottomSheet(val message: String) : Dialog() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(onDismissRequest: () -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(message)
            }
        }
    }
}
```

### Navigation from Composables

`HomeScreen.kt`:

```kotlin
@Composable
fun HomeScreen() {
    val router = LocalRouter.current
    Column {
        Button(onClick = {
        	router.navigateTo(Products)
        }) {
            Text("Products")
        }
    }
}
```

`Products.kt`:

```kotlin
@Composable
fun ProductListScreen() {
    val router = LocalRouter.current
    Column {
        Button(onClick = { 
        	router.showDialog(BottomSheet("These are Product details")) 
        }) {
            Text("Show Details")
        }
        Button(onClick = { router.exit() }) {
            Text("Back")
        }
    }
}
```

## Usage With Hilt (Dependency Injection)

### Provide Pathfinder Singleton

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    fun providePathfinder(): Pathfinder<Router> = Pathfinder.create(Router())
}
```

### Provide Router

```kotlin
@Module
@InstallIn(ActivityComponent::class, ViewModelComponent::class)
abstract class AppModule {
    companion object {
        @Provides
        fun provideRouter(pathfinder: Pathfinder<Router>): Router = pathfinder.router
    }
}
```

### Inject Pathfinder in Activity

```kotlin
@AndroidEntryPoint
class MainActivity : PathfinderActivity() {
    @Inject lateinit var pathfinder: Pathfinder<Router>
}
```

### Execute Navigation from ViewModel

```kotlin
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val router: Router
) : ViewModel() {

    fun navigateToNextScreen() {
        router.navigateTo(ProductDetails(Product(name = "Notebook")))
    }

    fun navigateBack() {
        router.exit()
    }
}
```

## Using `Router.launch` for Activity Result Contracts

Pathfinder allows you to safely launch **ActivityResultContracts** from your router, without manually handling `ActivityResultLauncher`.

### Step 1: Register Contracts in Your Activity

Override `registerContracts()` in your `PathfinderActivity` (e.g. `MainActivity`) to register all contracts you plan to use:

```kotlin
override fun registerContracts(registry: ActivityResultContractRegistry) {
    registry.register("RequestPermission", RequestPermission())
    registry.register("RequestPermissions", RequestMultiplePermissions())
    registry.register("PickFile", ActivityResultContracts.GetContent())
}
```

* Each contract is registered with a unique key.
* Pathfinder will automatically create launchers for these contracts and handle result callbacks.

### Step 2: Launch Contracts via `Router.launch`

You can now call `launch` from anywhere you have access to the `Router`.

#### Example: Request a Single Permission

```kotlin
private fun requestSinglePermission() {
    router.launch(
        key = "RequestPermission",
        input = Manifest.permission.CAMERA,
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with action
            } else {
                // Permission denied, handle accordingly
            }
        }
    )
}
```

#### Example: Request Multiple Permissions

```kotlin
private val multiplePermissions = arrayOf(
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.ACCESS_FINE_LOCATION
)

private fun requestMultiplePermissions() {
    router.launch(
        key = "RequestPermissions",
        input = multiplePermissions,
        onResult = { result: Map<String, Boolean> ->
            val isGranted = result.all { entry -> entry.value }
            if (isGranted) {
                // All permissions granted
            } else {
                // Handle denied permissions
            }
        }
    )
}
```

### Example: Pick a file

```kotlin
private fun pickFile() {
    router.launch(
        key = "PickFile",
        input = "*/*", // MIME type filter, e.g. "*/*" for all files
        onResult = { uri: Uri? ->
            if (uri != null) {
                // File was selected, handle the Uri
            } else {
                // User canceled
            }
        }
    )
}
```

### Important

* The `key` must match the one used when registering the contract.
* The `input` type must match the contract’s expected input.
* The `onResult` callback receives the contract result type.
* Pathfinder internally manages the `ActivityResultLauncher` lifecycle, so you don’t need to manually handle registration or lifecycle events.

## Passing Results Between Screens

Pathfinder allows screens to send and receive results in a type-safe manner, similar to `startActivityForResult`, but fully integrated with the router.

### Step 1: Set a Result Listener

On the originating screen, register a listener before navigating:

```kotlin
private fun navigateToNextScreen() {
    router.setResultListener<String>("result_message") { message ->
        showBottomSheet(message)
    }
    router.navigateTo(Destination)
}
```

* `"result_message"` is the unique key used to identify the result.
* The lambda receives the result when the target screen calls `sendResult`.


### Step 2: Send a Result

On the destination screen, send the result back:

```kotlin
router.sendResult(
    key = "result_message",
    data = "This is a result from another screen"
)
```

* `key` must match the one used in `setResultListener`.
* `data` can be of any type; Pathfinder ensures type-safety if you use generics correctly.

## Why Screens Should Be `@Serializable`

The Pathfinder back stack is created using Navigation 3 `rememberNavBackStack`, which persists state across configuration changes (like rotation).

* To support this persistence, each screen must have a unique key that implements `NavKey`.
* Additionally, any data contained in the screen must be serializable using Kotlin Serialization. This ensures the back stack is saved and restored correctly when the activity is recreated.

```kotlin
@Serializable
object Home : ComposeScreen() {
    @Composable
    override fun Content() = HomeScreen()
}
```

* `Home` implements `NavKey` (inherited from `Screen`), providing a unique `screenKey`.
* Marking it `@Serializable` ensures that the back stack can be safely restored on configuration changes.

> **Rule of Thumb:**
> Any screen that is part of the back stack and should survive configuration changes must implement `NavKey` and be `@Serializable`.
>

## API Reference

### `Router`

* `navigateTo(screen: Screen)` – Push a screen onto the back stack.
* `replaceScreen(screen: Screen)` – Replace the current screen.
* `backToScreen(screen: Screen, inclusive: Boolean)` – Go back to a specific screen.
* `backToScreenByKey(screenKey: String, inclusive: Boolean)` – Same as above, by key.
* `exit()` – Pop the current screen.
* `newScreenChain(vararg screens: Screen)` – Replace the back stack.
* `showDialog(dialog: Dialog)` – Show a dialog.
* `dismissDialog()` – Dismiss the current dialog.
* `launch(key: String, input: I, onResult: (O) -> Unit)` – Launch an activity contract.
* `sendResult(key: String, data: Any?)` – Send a result to a previously registered listener.
* `setResultListener(key: String, listener: ResultListener<R>)` – Listen for a result sent with `sendResult`. Returns a `ResultListenerHandler` that can be used to remove the listener.

### `Pathfinder`

* `create()` – Creates a default Pathfinder instance.
* `create(customRouter: T)` – Creates Pathfinder with a custom router.
* `getNavigatorHolder()` – Get the `NavigatorHolder` to attach/detach navigator.

### `PathfinderActivity`

* `launch(key: String, input: I, callback: (O) -> Unit)` – Launch registered contracts.
* `registerContracts(registry: ActivityResultContractRegistry)` – Abstract method to register contracts.

## Sample Project / Demo

To see Pathfinder in action, including screen navigation, dialogs, and activity result contracts, you can check out the [sample](https://github.com/ampfarisaho/pathfinder/tree/main/sample) project.

The sample demonstrates:

* Compose screens and dialogs (`ComposeScreen`, `Dialog`)
* Navigation using `Router` and `PathfinderNavigator`
* Using `Router.launch` with single/multiple permissions contracts
* Integration with Hilt for dependency injection
* Back stack management and configuration change handling
* Sending and recieving results from another screen
* Passing arguments to screens and view models

## Credits

Pathfinder is inspired by [Cicerone](https://github.com/terrakok/Cicerone), created by Konstantin Tskhovrebok ([@terrakok](https://github.com/terrakok/Cicerone)).

The architectural approach is intentionally similar, and I extend my thanks to Konstantin for the original work, which laid the foundation for Pathfinder.

## License

```
MIT License

Copyright (c) 2025 Ampfarisaho Maphangwa

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
