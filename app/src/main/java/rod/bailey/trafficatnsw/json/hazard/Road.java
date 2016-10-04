package rod.bailey.trafficatnsw.json.hazard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.util.JSONUtils;
import rod.bailey.trafficatnsw.util.MLog;

import static rod.bailey.trafficatnsw.util.JSONUtils.*;

public class Road {

	private static final String TAG = Road.class.getSimpleName();

	private String conditionTendency;
	private String crossStreet;
	private String delay;
	private String locationQualifier;
	private String mainStreet;
	private String region;
	private String secondLocation;
	private String suburb;
	private String trafficVolume;
	private int queueLength;
	private String fullStreetAddress;
	private List<Lane> impactedLanes;

	public Road(JSONObject json) {
		assert json != null;

		conditionTendency = safeGetString(json, "conditionTendency");
		crossStreet = safeGetString(json, "crossStreet");
		delay = safeGetString(json, "delay");
		locationQualifier = safeGetString(json, "locationQualifier");
		mainStreet = safeGetString(json, "mainStreet");
		region = safeGetString(json, "region");
		secondLocation = safeGetString(json, "secondLocation");
		suburb = safeGetString(json, "suburb");
		trafficVolume = safeGetString(json, "trafficVolume");
		queueLength = safeGetInt(json, "queueLength");

		// The 'fullStreetAddress' is pieced together from these properties:
		// mainStreet, locationQualifier, crossStreet, secondLocation.
		// The only mandatory property is mainStreet

		StringBuffer fullStr = new StringBuffer(mainStreet);

		if (isNonEmpty(crossStreet)) {
			fullStr.append(" ");
			fullStr.append(locationQualifier);
			fullStr.append(" ");
			fullStr.append(crossStreet);
		}

		if (isNonEmpty(secondLocation)) {
			fullStr.append(" and ");
			fullStr.append(secondLocation);
		}

		fullStreetAddress = fullStr.toString();

		// Parse out the Lanes objects

		List<Lane> lanes = new LinkedList<Lane>();
		JSONArray lanesJSONArray = JSONUtils.safeGetJSONArray(json,
				"impactedLanes");

		for (int i = 0; i < lanesJSONArray.length(); i++) {
			try {
				JSONObject laneJsonObject = lanesJSONArray.getJSONObject(i);
				Lane lane = new Lane(laneJsonObject);
				lanes.add(lane);
			} catch (JSONException e) {
				MLog.w(TAG, "Failed to parse Lane from JSON object", e);
			}
		}

		impactedLanes = lanes;
	}

	public String getConditionTendency() {
		return conditionTendency;

	}

	public String getCrossStreet() {
		return crossStreet;
	}

	public String getDelay() {
		return delay;
	}

	public String getLocationQualifier() {
		return locationQualifier;
	}

	public String getMainStreet() {
		return mainStreet;
	}

	public String getRegion() {
		return region;
	}

	public String getSecondLocation() {
		return secondLocation;
	}

	public String getSuburb() {
		return suburb;
	}

	public String getTrafficVolume() {
		return trafficVolume;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public String getFullStreetAddress() {
		return fullStreetAddress;
	}

	public List<Lane> getImpactedLanes() {
		return impactedLanes;
	}

}
