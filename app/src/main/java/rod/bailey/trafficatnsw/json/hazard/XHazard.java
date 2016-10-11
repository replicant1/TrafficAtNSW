package rod.bailey.trafficatnsw.json.hazard;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.util.JSONUtils;
import rod.bailey.trafficatnsw.util.MLog;

import static rod.bailey.trafficatnsw.util.JSONUtils.*;

public class XHazard implements Comparable<XHazard> {

	private static final String TAG = XHazard.class.getSimpleName();

	public static List<XHazard> createHazardsFromIncidentJsonContents(
			String jsonContents) {
		assert jsonContents != null;

		List<XHazard> result = new LinkedList<XHazard>();

		try {
			JSONObject topLevelObject = new JSONObject(jsonContents);
			JSONArray features = topLevelObject.getJSONArray("features");

			MLog.d(TAG,
					"Number of features in hazards JSON file: "
							+ features.length());

			for (int i = 0; i < features.length(); i++) {
				JSONObject featureJSONObject = features.getJSONObject(i);
				XHazard hazard = new XHazard(featureJSONObject);
				result.add(hazard);
			}
		} catch (JSONException e) {
			MLog.e(TAG, "Failed to parse JSON in hazards file", e);
		}

		return result;
	}

	private String adviceA;
	private String adviceB;
	private List<XArrangementElement> arrangementElements;

	private List<String> attendingGroups;

	private Date created;
	private String displayName;
	private Date end;
	private boolean ended;

	private int hazardId;
	private String headline;
	private boolean impactingNetwork;

	private boolean isInitialReport;
	private boolean isMajor;
	private Date lastUpdated;
	private Location latlng;
	private String mainCategory;
	private String otherAdvice;
	private List<XPeriod> periods;
	private JSONObject properties;
	private String publicTransport;
	private List<XRoad> roads;

	private Date start;
	private String subCategoryA;
	private String subCategoryB;
	private String webLinkName;

	private String webLinkUrl;

	public XHazard(JSONObject json) {
		assert json != null;

		properties = safeGetJSONObject(json, "properties");
		headline = safeGetString(properties, "headline");
		lastUpdated = null; // ?
		hazardId = JSONUtils.safeGetInt(json, "id");

		JSONObject geometryProperties = safeGetJSONObject(json, "geometry");
		JSONArray coordinates = safeGetJSONArray(geometryProperties,
				"coordinates");
		double firstCoord = safeGetDoubleArrayElement(coordinates, 0);
		double secondCoord = safeGetDoubleArrayElement(coordinates, 1);

		latlng = new Location("");
		latlng.setLatitude(secondCoord);
		latlng.setLongitude(firstCoord);

		isInitialReport = safeGetBoolean(properties, "isInitialReport");
		isMajor = safeGetBoolean(properties, "isMajor");
		ended = safeGetBoolean(properties, "ended");
		impactingNetwork = safeGetBoolean(properties, "impactingNetwork");

		created = safeGetDate(properties, "created");
		start = safeGetDate(properties, "start");
		end = safeGetDate(properties, "end");
		lastUpdated = safeGetDate(properties, "lastUpdated");

		mainCategory = safeGetString(properties, "mainCategory");
		subCategoryA = safeGetString(properties, "subCategoryA");
		subCategoryB = safeGetString(properties, "subCategoryB");
		adviceA = safeGetString(properties, "adviceA");
		adviceB = safeGetString(properties, "adviceB");
		otherAdvice = safeGetString(properties, "otherAdvice");
		webLinkUrl = safeGetString(properties, "webLinkUrl");
		webLinkName = safeGetString(properties, "webLinkName");
		displayName = safeGetString(properties, "displayName");
		publicTransport = safeGetString(properties, "publicTransport");

		parseAttendingGroups();
		parseRoads();
		parsePeriods();
		parseArrangementElements();
	}

	public String getAdviceA() {
		return adviceA;
	}

	public String getAdviceB() {
		return adviceB;
	}

	public List<XArrangementElement> getArrangementElements() {
		return arrangementElements;
	}

	public List<String> getAttendingGroups() {
		return attendingGroups;
	}

	public Date getCreated() {
		return created;
	}

	public String getDisplayName() {
		return displayName;
	}

	public Date getEnd() {
		return end;
	}

	public int getHazardId() {
		return hazardId;
	}

	public String getHeadline() {
		return headline;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Location getLatlng() {
		return latlng;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public String getOtherAdvice() {
		return otherAdvice;
	}

	public List<XPeriod> getPeriods() {
		return periods;
	}

	public JSONObject getProperties() {
		return properties;
	}

	public String getPublicTransport() {
		return publicTransport;
	}

	public List<XRoad> getRoads() {
		return roads;
	}

	public Date getStart() {
		return start;
	}

	public String getSubCategoryA() {
		return subCategoryA;
	}

	public String getSubCategoryB() {
		return subCategoryB;
	}

	public String getWebLinkName() {
		return webLinkName;
	}

	public String getWebLinkUrl() {
		return webLinkUrl;
	}

	public boolean isEnded() {
		return ended;
	}

	public boolean isImpactingNetwork() {
		return impactingNetwork;
	}

	public boolean isInitialReport() {
		return isInitialReport;
	}

	public boolean isMajor() {
		return isMajor;
	}

	private void parseArrangementElements() {
		// Parse ArrangementElements array
		arrangementElements = new LinkedList<XArrangementElement>();
		JSONArray elementsJsonObjects = JSONUtils.safeGetJSONArray(properties,
				"arrangementElements");

		if (elementsJsonObjects != null) {
			for (int i = 0; i < elementsJsonObjects.length(); i++) {

				JSONObject elementJsonObject = JSONUtils
						.safeGetJSONObjectArrayElement(elementsJsonObjects, i);
				if (elementJsonObject != null) {
					XArrangementElement element = new XArrangementElement(
							elementJsonObject);
					arrangementElements.add(element);
				}
			}
		}

		assert arrangementElements != null;
	}

	private void parseAttendingGroups() {
		// "Attending groups"
		JSONArray attendingGroupsJSONArray = JSONUtils.safeGetJSONArray(
				properties, "attendingGroups");
		attendingGroups = new LinkedList<String>();

		if (attendingGroupsJSONArray != null) {
			for (int i = 0; i < attendingGroupsJSONArray.length(); i++) {
				String groupStr = JSONUtils.safeGetStringArrayElement(
						attendingGroupsJSONArray, i);
				if (groupStr != null) {
					attendingGroups.add(groupStr);
				}
			}
		}

		assert attendingGroups != null;
	}

	private void parsePeriods() {
		// Parse "periods" array
		periods = new LinkedList<XPeriod>();
		JSONArray periodsJsonObjects = JSONUtils.safeGetJSONArray(properties,
				"periods");

		if (periodsJsonObjects != null) {
			for (int i = 0; i < periodsJsonObjects.length(); i++) {
				JSONObject periodJsonObject = JSONUtils
						.safeGetJSONObjectArrayElement(periodsJsonObjects, i);

				if (periodJsonObject != null) {
					XPeriod period = new XPeriod(periodJsonObject);
					periods.add(period);
				}
			}
		}

		assert periods != null;
	}

	private void parseRoads() {
		// Parse "roads" array
		roads = new LinkedList<XRoad>();
		JSONArray roadJsonObjects = JSONUtils.safeGetJSONArray(properties,
				"roads");

		if (roadJsonObjects != null) {
			for (int j = 0; j < roadJsonObjects.length(); j++) {
				JSONObject roadJsonObject = JSONUtils
						.safeGetJSONObjectArrayElement(roadJsonObjects, j);

				if (roadJsonObject != null) {
					XRoad road = new XRoad(roadJsonObject);
					roads.add(road);
				}
			}
		}

		assert roads != null;
	}

	@Override
	/**
	 * @reutn a negative integer if this < another
	 * a positive integer if this > another
	 * 0 if this .euqals another
	 */
	public int compareTo(XHazard another) {
		int result = 0;

		if ((lastUpdated != null) && (another.lastUpdated != null)) {
			result = lastUpdated.compareTo(another.lastUpdated) * -1;
		} else if ((lastUpdated == null) && (another.lastUpdated != null)) {
			result = 0;
		}
		
		return result;
	}
}
