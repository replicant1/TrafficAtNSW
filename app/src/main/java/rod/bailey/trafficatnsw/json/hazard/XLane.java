package rod.bailey.trafficatnsw.json.hazard;

import org.json.JSONObject;

import static rod.bailey.trafficatnsw.util.JSONUtils.*;

public class XLane {

	private String affectedDirection;
	private String closedLanes;
	private String description;
	private String extent;
	private String numberOfLanes;

	private String roadType;

	public XLane(JSONObject json) {
		assert json != null;

		affectedDirection = safeGetString(json, "affectedDirection");
		closedLanes = safeGetString(json, "closedLanes");
		description = safeGetString(json, "description");
		extent = safeGetString(json, "extent");
		numberOfLanes = safeGetString(json, "numberOfLanes");
		roadType = safeGetString(json, "roadType");
	}

	public String getAffectedDirection() {
		return affectedDirection;
	}

	public String getClosedLanes() {
		return closedLanes;
	}

	public String getDescription() {
		return description;
	}

	public String getExtent() {
		return extent;
	}

	public String getNumberOfLanes() {
		return numberOfLanes;
	}

	public String getRoadType() {
		return roadType;
	}
}
