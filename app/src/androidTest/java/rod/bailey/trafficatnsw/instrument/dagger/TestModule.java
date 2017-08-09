package rod.bailey.trafficatnsw.instrument.dagger;

import android.support.test.espresso.core.deps.dagger.Provides;

import javax.inject.Singleton;

import dagger.Module;
import rod.bailey.trafficatnsw.service.IDataService;
import rod.bailey.trafficatnsw.instrument.service.TestDataService;

/**
 * Created by rodbailey on 8/8/17.
 */
@Module
public class TestModule {

	@Singleton
	@Provides
	public IDataService provideIDataService() {
		return new TestDataService();
	}
}
