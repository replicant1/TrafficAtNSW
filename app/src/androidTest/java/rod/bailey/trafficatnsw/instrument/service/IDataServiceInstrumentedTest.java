package rod.bailey.trafficatnsw.instrument.service;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import rod.bailey.trafficatnsw.app.AppModule;
import rod.bailey.trafficatnsw.app.MainActivity_;
import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication_;
import rod.bailey.trafficatnsw.instrument.dagger.DaggerActivityTestRule;
import rod.bailey.trafficatnsw.instrument.dagger.DaggerTestComponent;
import rod.bailey.trafficatnsw.instrument.dagger.TestComponent;
import rod.bailey.trafficatnsw.instrument.dagger.TestDataServiceModule;

import static junit.framework.Assert.assertTrue;

/**
 * Created by rodbailey on 9/8/17.
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IDataServiceInstrumentedTest {

	private TestComponent testComponent;

	@Rule
	public ActivityTestRule<MainActivity_> mActivityRule = new DaggerActivityTestRule<MainActivity_>(
			MainActivity_.class, new BeforeActivityLaunchedListener());

	/**
	 * Checks that an @Inject results in an instance of TestDataService rather than RemoteDataService.
	 */
	@Test
	public void testIDataService() {
		TestDataServiceHolder holder = new TestDataServiceHolder();
		assertTrue(holder.getDataService() instanceof TestDataService);
	}

	private class BeforeActivityLaunchedListener implements DaggerActivityTestRule.OnBeforeActivityLaunchedListener<MainActivity_> {
		@Override
		public void beforeActivityLaunched(@NonNull Application application, @NonNull MainActivity_ activity) {
			testComponent = DaggerTestComponent.builder()
					.appModule(new AppModule(application))
					.testDataServiceModule(new TestDataServiceModule())
					.build();
			((TrafficAtNSWApplication_) application).Companion.setGraph(testComponent);
		}
	}
}
