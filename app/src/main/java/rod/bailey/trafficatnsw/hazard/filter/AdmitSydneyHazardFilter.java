package rod.bailey.trafficatnsw.hazard.filter;

import java.util.List;

import rod.bailey.trafficatnsw.json.hazard.XHazard;
import rod.bailey.trafficatnsw.json.hazard.XRegion;
import rod.bailey.trafficatnsw.json.hazard.XRoad;
import rod.bailey.trafficatnsw.util.MLog;

public class AdmitSydneyHazardFilter implements IHazardFilter {

	private static final String TAG = AdmitSydneyHazardFilter.class
			.getSimpleName();

	@Override
	public boolean admit(XHazard hazard) {
		assert hazard != null;

		boolean result = false;
		List<XRoad> roads = hazard.getRoads();

		if ((roads != null) && (!roads.isEmpty())) {
			XRoad road = roads.get(0);
			String roadRegionStr = road.getRegion();

			if (roadRegionStr != null) {
				try {
					XRegion roadRegion = XRegion.valueOf(roadRegionStr);
					if (roadRegion != null) {
						result = roadRegion.isSydney();
					}
				} catch (Throwable thr) {
					MLog.w(TAG,
							"Found a region sring that could not be decoded to an enum element: "
									+ roadRegionStr, thr);
				}
			}
		}

		return result;
	}

}
