package rod.bailey.trafficatnsw.instrument.dagger;

import javax.inject.Singleton;

import dagger.Component;
import rod.bailey.trafficatnsw.instrument.service.TestDataService;
import rod.bailey.trafficatnsw.instrument.service.TestDataServiceHolder;
import rod.bailey.trafficatnsw.app.AppComponent;
import rod.bailey.trafficatnsw.app.AppModule;

/**
 * Created by rodbailey on 8/8/17.
 */
@Singleton
@Component(modules = {AppModule.class, TestDataServiceModule.class})
public interface TestComponent extends AppComponent {
	void inject(TestDataService service);
	void inject(TestDataServiceHolder obj);
}
