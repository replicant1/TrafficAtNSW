package rod.bailey.trafficatnsw.cameras.filter;

import rod.bailey.trafficatnsw.cameras.TrafficCamera;

public class AdmitRegionalTrafficCameraFilter implements ITrafficCameraFilter {

	@Override
	public boolean admit(TrafficCamera camera) {
		return (camera.getRegion().isRegional());
	}

}
