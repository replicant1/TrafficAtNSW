package rod.bailey.trafficatnsw;

import javax.inject.Inject;

import rod.bailey.trafficatnsw.app.TrafficAtNSWApplication_;
import rod.bailey.trafficatnsw.common.service.IDataService;

/**
 * Created by rodbailey on 9/8/17.
 */

public class TestDataServiceHolder {

	@Inject
	IDataService dataService;

	public TestDataServiceHolder() {
		((TestComponent) TrafficAtNSWApplication_.Companion.getGraph()).inject(this);
	}
}
