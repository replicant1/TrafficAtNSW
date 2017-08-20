package rod.bailey.trafficatnsw.instrument.util;

import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.cameras.data.XCamera;
import rod.bailey.trafficatnsw.hazard.data.XHazard;
import rod.bailey.trafficatnsw.traveltime.data.MotorwayTravelTimesStore;
import rod.bailey.trafficatnsw.traveltime.data.SegmentId;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeProperties;
import rod.bailey.trafficatnsw.traveltime.data.XTravelTimeSegment;
import rod.bailey.trafficatnsw.traveltime.item.HeadingTTListItem;
import rod.bailey.trafficatnsw.traveltime.item.ITTListItem;
import rod.bailey.trafficatnsw.traveltime.item.SimpleTTListItem;

import static junit.framework.Assert.assertTrue;

/**
 * Utility methods for tests
 */
public class TestUtils {

	public static XCamera findCameraById(List<XCamera> cameras, String cameraId) {
		XCamera result = null;
		for (XCamera camera: cameras) {
			if (cameraId.equals(camera.getId())) {
				result = camera;
				break;
			}
		}
		return result;
	}

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

	/**
	 * @return A new segment with the given SegmentId and travelTime, which is active and included
	 * in the total.
	 */
	public static XTravelTimeSegment newSegment(String segmentIdStr, int travelTime) {
		SegmentId segmentId = SegmentId.Companion.parse(segmentIdStr);
		XTravelTimeProperties props = new XTravelTimeProperties(
				segmentId.getDirection().getApiToken(),
				segmentId.getDirection().getApiToken() + "_to",
				true,
				segmentId.getDirection().getApiToken() + "_from");
		props.setTravelTimeMinutes(travelTime);
		XTravelTimeSegment segment = new XTravelTimeSegment(segmentId.toApiToken(), null, props);
		segment.setIncludedInTotalSilently(true);
		return segment;
	}

	public static XTravelTimeSegment newTotalSegment(String segmentIdStr, int travelTime) {
		XTravelTimeSegment seg = newSegment(segmentIdStr, travelTime);
		seg.setIncludedInTotalSilently(true);
		return seg;
	}

	public static HeadingTTListItem getHeading(int index, LinkedList<ITTListItem> items) {
		ITTListItem listItem = items.get(index);
		assertTrue(listItem instanceof HeadingTTListItem);
		return (HeadingTTListItem) listItem;
	}

	public static SimpleTTListItem getSimple(int index, LinkedList<ITTListItem> items) {
		ITTListItem listItem = items.get(index);
		assertTrue(listItem instanceof SimpleTTListItem);
		return (SimpleTTListItem) listItem;
	}

	public static int getSimpleTravelTime(int index, LinkedList<ITTListItem> items) {
		ITTListItem listItem = items.get(index);
		assertTrue(listItem instanceof SimpleTTListItem);
		return ((SimpleTTListItem) listItem).getTravelTime().getProperties().getTravelTimeMinutes();
	}

	public static void assertIsHeading(int index, LinkedList<ITTListItem> items) {
		assertTrue(items.get(index) instanceof HeadingTTListItem);
	}

	public static void assertIsSimple(int index, LinkedList<ITTListItem> items) {
		assertTrue(items.get(index) instanceof  SimpleTTListItem);
	}

	public static void assertIsTotal(int index, LinkedList<ITTListItem> items) {
		assertTrue( ((SimpleTTListItem) items.get(index)).getTravelTime().isTotal());
	}
}
