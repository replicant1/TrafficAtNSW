package rod.bailey.trafficatnsw.instrument.util;

import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeProperties;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.traveltime.item.HeadingTTListItem;
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem;
import rod.bailey.trafficatnsw.traveltime.item.SimpleTTListItem;

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
			if (segmentId.equals(segment.getSegmentId())) {
				result = segment;
				break;
			}
		}
		return result;
	}

	public static String dumpITTList(LinkedList<ITTListItem> items) {
		StringBuffer buf = new StringBuffer();

		int i = 0;
		for (ITTListItem item: items) {
			if (item instanceof HeadingTTListItem) {
				buf.append("=================\n");
				buf.append("[" + i + "] " + ((HeadingTTListItem) item).getText() + "\n");
				buf.append("=================\n");
			}
			else if (item instanceof SimpleTTListItem) {
				SimpleTTListItem simpleItem = (SimpleTTListItem) item;
				XTravelTimeSegment seg = simpleItem.getTravelTime();
				XTravelTimeProperties props = seg.getProperties();

				buf.append("[" + i + "] " + seg.getSegmentId() + ": ");
				if (props != null) {
					buf.append(String.format("(%s - %s) ",
							props.getFromDisplayName(),
							props.getToDisplayName()));
					buf.append(props.getTravelTimeMinutes() + " mins");
					buf.append(" - " + (Boolean.TRUE.equals(props.isActive()) ? "active" : "inactive"));
					buf.append(" included=" + Boolean.TRUE.equals(((SimpleTTListItem) item).getTravelTime()
							.getIncludedInTotal()));
				}
				buf.append("\n");
			}
			else {
				buf.append("Unrecognized TTListItem\n");
			}
			i++;
		} // for

		return buf.toString();
	}
}
