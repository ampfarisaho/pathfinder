package za.ampfarisaho.pathfinder.sample.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import za.ampfarisaho.pathfinder.Router
import za.ampfarisaho.pathfinder.sample.common.CountriesLoader
import za.ampfarisaho.pathfinder.sample.navigation.PathTwo

@HiltViewModel(assistedFactory = PathOneViewModel.Factory::class)
class PathOneViewModel @AssistedInject constructor(
    @Assisted private val countryCode: String,
    private val countriesLoader: CountriesLoader,
    private val router: Router
) : ViewModel() {

    private val _countryName = MutableStateFlow("")
    val countryName: StateFlow<String> = _countryName

    init {
        viewModelScope.launch {
            val country = countriesLoader.loadCountries().find { it.code == countryCode }
            if (country == null) {
                throw NoSuchElementException("No country found with code: $countryCode")
            }
            _countryName.value = country.name
        }
    }

    fun goBack() {
        router.exit()
    }

    fun goToPathTwo() {
        router.navigateTo(PathTwo)
    }

    @AssistedFactory
    interface Factory {
        fun create(countryCode: String): PathOneViewModel
    }
}
