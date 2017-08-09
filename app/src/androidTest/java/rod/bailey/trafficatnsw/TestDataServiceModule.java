package rod.bailey.trafficatnsw;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rod.bailey.trafficatnsw.common.service.IDataService;
import rod.bailey.trafficatnsw.common.service.TestDataService;

/**
 * Created by rodbailey on 8/8/17.
 */
@Module
public class TestDataServiceModule {

	@Provides
	@Singleton
	public IDataService provideIDataService() {
		return new TestDataService();
	}
}
