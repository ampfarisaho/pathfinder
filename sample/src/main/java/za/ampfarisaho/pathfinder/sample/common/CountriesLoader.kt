package za.ampfarisaho.pathfinder.sample.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.util.Locale
import javax.inject.Inject

interface CountriesLoader {
    suspend fun loadCountries(): List<Country>
}

class CountriesLoaderImpl @Inject constructor() : CountriesLoader {
    override suspend fun loadCountries(): List<Country> = withContext(Dispatchers.IO) {
        Locale.getISOCountries()
            .map { countryCode ->
                val locale = Locale.Builder().setRegion(countryCode).build()
                Country(
                    name = locale.displayCountry,
                    code = countryCode
                )
            }
            .filter { it.name.isNotBlank() }
            .sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
    }
}

@Serializable
class Country(val name: String, val code: String)
