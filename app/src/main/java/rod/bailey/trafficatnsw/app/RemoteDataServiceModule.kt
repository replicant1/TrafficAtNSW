package rod.bailey.trafficatnsw.app

import android.app.Application
import dagger.Module
import dagger.Provides
import rod.bailey.trafficatnsw.common.service.IDataService
import rod.bailey.trafficatnsw.common.service.RemoteDataService
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