package za.ampfarisaho.pathfinder.sample.injection

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import za.ampfarisaho.pathfinder.Pathfinder
import za.ampfarisaho.pathfinder.Router
import za.ampfarisaho.pathfinder.sample.common.CountriesLoader
import za.ampfarisaho.pathfinder.sample.common.CountriesLoaderImpl
import za.ampfarisaho.pathfinder.sample.common.PermissionLookup
import za.ampfarisaho.pathfinder.sample.common.PermissionLookupImpl

@Module
@InstallIn(
    ActivityComponent::class,
    ViewModelComponent::class
)
abstract class AppModule {
    @Binds
    abstract fun bindCountriesLoader(loader: CountriesLoaderImpl): CountriesLoader

    @Binds
    abstract fun bindPermissionLookup(lookup: PermissionLookupImpl): PermissionLookup

    companion object {
        @Provides
        fun provideRouter(pathfinder: Pathfinder<Router>): Router = pathfinder.router
    }
}
