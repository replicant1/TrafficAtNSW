package rod.bailey.trafficatnsw;

import java.util.List;

import rod.bailey.trafficatnsw.hazard.data.XHazard;

/**
 * Utility methods for tests
 */
public class TestUtils {

	public static XHazard findHazardById(List<XHazard> hazards, int hazardId) {
		XHazard result = null;
		for (XHazard hazard : hazards) {
			if (hazardId == hazard.getHazardId().intValue()) {
				result = hazard;
				break;
			}
		}
		return result;
	}
}