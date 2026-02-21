package za.ampfarisaho.pathfinder.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import za.ampfarisaho.pathfinder.Router
import za.ampfarisaho.pathfinder.common.CountriesLoader
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
) : ViewModel() {

    fun navigateToPathOne() {
        viewModelScope.launch {
            val countryCode = getRandomCountryCode()
            router.navigateTo(PathOne(countryCode))
        }
    }

    private suspend fun getRandomCountryCode(): String {
        val countries = countriesLoader.loadCountries()
        if (countries.isEmpty()) {
            throw NoSuchElementException("No countries available to pick from")
        }
        return countries.random(Random).code
    }

    fun navigateToResultPath() {
        router.setResultListener<String>("result_message") { message ->
            showBottomSheet(message)
        }
        router.navigateTo(ResultPath)
    }

    fun navigateToActivityTwo() {
        router.navigateTo(ActivityTwo)
    }

    fun showDialog() {
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

    fun showBottomSheet(message: String = "Bottom Sheet") {
        router.showDialog(BottomSheet(message))
    }
}
