package rod.bailey.trafficatnsw.instrument.dagger;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rod.bailey.trafficatnsw.service.IDataService;
import rod.bailey.trafficatnsw.instrument.service.TestDataService;

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
