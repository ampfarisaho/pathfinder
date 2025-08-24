package za.ampfarisaho.pathfinder.screens

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.ampfarisaho.pathfinder.Router
import za.ampfarisaho.pathfinder.common.CountriesLoader
import za.ampfarisaho.pathfinder.common.PermissionLookup
import za.ampfarisaho.pathfinder.navigation.ActivityTwo
import za.ampfarisaho.pathfinder.navigation.BottomSheet
import za.ampfarisaho.pathfinder.navigation.PathOne
import za.ampfarisaho.pathfinder.navigation.ResultPath
import za.ampfarisaho.pathfinder.navigation.WarningDialog
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val router: Router,
    private val countriesLoader: CountriesLoader,
    private val permissionLookup: PermissionLookup
) : ViewModel() {

    private val singlePermission = Manifest.permission.CAMERA
    private val multiPermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val staticButtons = listOf(
        HomeButton(text = "Navigate Screens", onClick = ::navigateToPathOne),
        HomeButton(text = "Open Result Screen", onClick = ::navigateToResultPath),
        HomeButton(text = "Open Activity 2", onClick = ::navigateToActivityTwo),
        HomeButton(text = "Show Dialog", onClick = ::showDialog),
        HomeButton(text = "Show Bottom Sheet", onClick = ::showBottomSheet)
    )

    private val _buttons = MutableStateFlow<List<HomeButton>>(listOf())
    val buttons: StateFlow<List<HomeButton>> = _buttons

    init {
        updateButtons()
    }

    private fun updateButtons() {
        val permissionButtons = listOf(
            HomeButton(
                text = "Request Single Permission",
                enabled = !permissionLookup.isGranted(singlePermission),
                onClick = ::requestSinglePermission
            ),
            HomeButton(
                text = "Request Multiple Permissions",
                enabled = !multiPermissions.all(permissionLookup::isGranted),
                onClick = ::requestMultiplePermissions
            )
        )

        _buttons.value = staticButtons + permissionButtons
    }

    private fun navigateToPathOne() {
        viewModelScope.launch {
            val countryCode = getRandomCountryCode()
            router.navigateTo(PathOne(countryCode))
        }
    }

    suspend fun getRandomCountryCode(): String {
        val countries = countriesLoader.loadCountries()
        if (countries.isEmpty()) {
            throw NoSuchElementException("No countries available to pick from")
        }
        return countries.random(Random).code
    }

    private fun navigateToResultPath() {
        router.setResultListener<String>("result_message") { message ->
            showBottomSheet(message)
        }
        router.navigateTo(ResultPath)
    }

    private fun navigateToActivityTwo() {
        router.navigateTo(ActivityTwo)
    }

    private fun showDialog() {
        router.showDialog(
            WarningDialog(
                title = "Pathfinder",
                message = "Explore untraversed regions to mark out a new route",
                positiveButtonText = "Continue",
                positiveButtonClick = {
                    navigateToPathOne()
                },
                negativeButtonText = "Cancel"
            )
        )
    }

    private fun showBottomSheet(message: String = "Bottom Sheet") {
        router.showDialog(BottomSheet(message))
    }

    private fun requestSinglePermission() {
        router.launch(
            key = "RequestPermission",
            input = singlePermission,
            onResult = { isGranted: Boolean ->
                if (isGranted) {
                    updateButtons()
                }
            }
        )
    }

    private fun requestMultiplePermissions() {
        router.launch(
            key = "RequestPermissions",
            input = multiPermissions,
            onResult = { result: Map<String, Boolean> ->
                val isGranted = result.all { entry -> entry.value }
                if (isGranted) {
                    updateButtons()
                }
            }
        )
    }
}
