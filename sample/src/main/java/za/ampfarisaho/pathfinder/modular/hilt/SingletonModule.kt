package za.ampfarisaho.pathfinder.modular.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import za.ampfarisaho.pathfinder.Pathfinder
import za.ampfarisaho.pathfinder.Router
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {
    @Provides
    @Singleton
    fun providePathfinder(): Pathfinder<Router> = Pathfinder.create(Router())
}
