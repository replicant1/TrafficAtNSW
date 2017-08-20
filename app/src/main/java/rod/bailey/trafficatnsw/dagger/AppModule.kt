package rod.bailey.trafficatnsw.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.app.ConfigSingleton
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication
import rod.bailey.trafficatnsw.cameras.data.TrafficCameraCacheSingleton
import rod.bailey.trafficatnsw.hazard.data.HazardCacheSingleton
import rod.bailey.trafficatnsw.traveltime.data.MotorwayConfigRegistry
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
	fun provideMotorwayConfigRegistry(): MotorwayConfigRegistry {
		return MotorwayConfigRegistry()
	}

	@Provides
	@Singleton
	fun provideConfigSingleton(): ConfigSingleton {
		return ConfigSingleton()
	}

	@Provides
	fun provideContext(): Context = TrafficAtNSWApplication.context

}