package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.cameras.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.hazard.HazardCacheSingleton
import rod.bailey.trafficatnsw.traveltime.common.TravelTimesCacheSingleton
import rod.bailey.trafficatnsw.util.ConfigSingleton
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