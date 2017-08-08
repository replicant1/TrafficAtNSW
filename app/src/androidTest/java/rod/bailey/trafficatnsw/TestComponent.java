package rod.bailey.trafficatnsw;

import javax.inject.Singleton;

import dagger.Component;
import rod.bailey.trafficatnsw.app.AppComponent;
import rod.bailey.trafficatnsw.app.AppModule;
import rod.bailey.trafficatnsw.app.MainActivity;
import rod.bailey.trafficatnsw.app.RemoteDataServiceModule;

/**
 * Created by rodbailey on 8/8/17.
 */
@Singleton
@Component(modules = {AppModule.class, TestDataServiceModule.class})
interface TestComponent extends AppComponent {

	void inject(TestDataServiceHolder obj);
}
