package rod.bailey.trafficatnsw;

import android.app.Application;
import android.support.test.espresso.core.deps.dagger.Provides;

import javax.inject.Singleton;

import dagger.Module;
import rod.bailey.trafficatnsw.app.AppModule;
import rod.bailey.trafficatnsw.common.service.IDataService;
import rod.bailey.trafficatnsw.common.service.TestDataService;

/**
 * Created by rodbailey on 8/8/17.
 */
@Module
public class TestModule  {

	@Singleton
	@Provides
	public IDataService provideIDataService() {
		return new TestDataService();
	}
}
