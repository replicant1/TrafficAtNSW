package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import rod.bailey.trafficatnsw.traveltime.data.TravelTimesCacheSingleton
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

	@Provides
	@Singleton
	fun provideApp() = app

	@Provides
	@Singleton
	fun provideHazardCacheSingleton(): HazardCacheSingleton {
		return HazardCacheSingleton()
	}

	@Provides
	@Singleton
	fun provideTrafficCameraCacheSingleton(): TrafficCameraCacheSingleton {
		return TrafficCameraCacheSingleton()
	}

	@Provides
	@Singleton
	fun provideTravelTimesCacheSingleton(): TravelTimesCacheSingleton {
		return TravelTimesCacheSingleton()
	}

	@Provides
	@Singleton
	fun provideConfigSingleton(): ConfigSingleton {
		return ConfigSingleton()
	}
}