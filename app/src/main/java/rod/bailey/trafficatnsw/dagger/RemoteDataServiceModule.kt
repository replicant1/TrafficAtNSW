package rod.bailey.trafficatnsw.dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.service.IDataService
import rod.bailey.trafficatnsw.service.RemoteDataService
import javax.inject.Singleton

/**
 * Created by rodbailey on 8/8/17.
 */
@Module
class RemoteDataServiceModule(val app: Application) {

	@Provides
	@Singleton
	fun provideIDataService(): IDataService {
		return RemoteDataService()
	}
}