package rod.bailey.trafficatnsw.cameras.filter;

import rod.bailey.trafficatnsw.cameras.TrafficCamera;

public interface ITrafficCameraFilter {

	/**
	 * @param camera
	 *            Traffic camera to be filtered. Must be non-null.
	 * @return true if the given camera is admitted (gets through) this filter
	 */
	public boolean admit(TrafficCamera camera);
}
