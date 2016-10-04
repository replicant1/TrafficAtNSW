package rod.bailey.trafficatnsw.cameras.filter;

import rod.bailey.trafficatnsw.cameras.TrafficCamera;

public class AdmitFavouritesTrafficCameraFilter implements ITrafficCameraFilter {

	@Override
	public boolean admit(TrafficCamera camera) {
		assert camera != null;
		return camera.isFavourite();
	}

}
