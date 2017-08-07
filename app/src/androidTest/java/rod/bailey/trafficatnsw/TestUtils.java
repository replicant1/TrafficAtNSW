package rod.bailey.trafficatnsw;

import java.util.List;

import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;

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

	public static XTravelTimeSegment findSegmentById(List<XTravelTimeSegment> segments, String segmentId) {
		XTravelTimeSegment result = null;

		for (XTravelTimeSegment segment : segments) {
			if (segmentId .equals(segment.getSegmentId())) {
				result = segment;
				break;
			}
		}
		return result;
	}
}
