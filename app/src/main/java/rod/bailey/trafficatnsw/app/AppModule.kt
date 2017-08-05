package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.common.service.IDataService
import rod.bailey.trafficatnsw.common.service.RemoteDataService
import rod.bailey.trafficatnsw.common.service.TestDataService
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

	@Provides
	@Singleton
	fun provideIDataService(): IDataService {
		// Use RemoteDataService to fetch from remote URL
		// Use TestDataService to fetch from files in the /assets directory
		return TestDataService()
	}
}