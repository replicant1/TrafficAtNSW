package rod.bailey.trafficatnsw.traveltime.common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

import rod.bailey.trafficatnsw.util.MLog;

public class TravelTime implements Comparable<TravelTime> {

	/**
	 * Only changes in value of the following properties are broadcast to
	 * registered PropertyChangeListeners
	 */
	public static final String PROPERTY_INCLUDED_IN_TOTAL = "includedInTotal";
	public static final String PROPERTY_TRAVEL_TIME_IN_MINUTES = "travelTimesInMinutes";

	private static final String TAG = TravelTime.class.getSimpleName();

	private final PropertyChangeSupport support = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	private void firePropertyChangeEvent(String propertyName, Object newValue) {
		support.firePropertyChange(propertyName, null, newValue);
	}

	/**
	 * Takes the contents of the remote JSON file as a single string and breaks
	 * it down into an array of TravelTime instances.
	 * 
	 * jsonFileContents - textual contents of JSON file return - array of
	 * TravelTime instances. Includes TOTAL features.
	 * 
	 * @param jsonFileContents
	 * @return List of TravelTIme instances filled out with data from the JSON
	 *         file. Order of list is same as order of JSON objects appearance
	 *         in the given jsonFileContents.
	 */
	public static List<TravelTime> parseTravelTimesFromJsonFile(
			String jsonFileContents) {
		assert jsonFileContents != null;
		List<TravelTime> result = new LinkedList<TravelTime>();

		try {
			MLog.INSTANCE.d(TAG, "Parsing top level object");

			JSONObject topLevelObject = new JSONObject(jsonFileContents);

			MLog.INSTANCE.d(TAG, "Parsed top level object OK");

			JSONArray features = topLevelObject.getJSONArray("features");

			MLog.INSTANCE.d(TAG, "Number of features in JSON file: " + features.length());
			for (int i = 0; i < features.length(); i++) {
				JSONObject featureJSONObject = features.getJSONObject(i);
				String featureId = featureJSONObject.getString("id");

				MLog.INSTANCE.d(TAG,
						String.format("Hazard[%d] has id %s", i, featureId));
				MLog.INSTANCE.d(TAG, String.format(
						"Creating a TravelTime object with id %s", featureId));

				TravelTime travelTime = new TravelTime(featureJSONObject);
				result.add(travelTime);
			}
		} catch (JSONException e) {
			MLog.INSTANCE.e(TAG, "Failed to parse JSON", e);
		}

		return result;
	}

	public static void testParseJsonFile(String jsonFilePath,
			String jsonFileType) {

	}

	private boolean active;
	private String fromDisplayName;
	private boolean includedInTotal;
	private String segmentId;
	private String toDisplayName;
	private int travelTimeMinutes;

	public TravelTime(JSONObject featureJSONObject) throws JSONException {
		assert featureJSONObject == null;

		JSONObject properties = featureJSONObject.getJSONObject("properties");

		// 'id' is mandatory
		try {
			segmentId = featureJSONObject.getString("id");
		} catch (JSONException e) {
			MLog.INSTANCE.e(TAG, "Failed to parse 'id' property of travel time segment",
					e);
		}

		// 'fromDisplayName' is mandatory
		try {
			fromDisplayName = properties.getString("fromDisplayName");
		} catch (JSONException e) {
			MLog.INSTANCE.d(TAG,
					"Failed to parse 'fromDisplayName' property of travel time segment");
		}

		// 'toDisplayName' is mandatory
		try {
			toDisplayName = properties.getString("toDisplayName");
		} catch (JSONException e) {
			MLog.INSTANCE.d(TAG,
					"Failed to parse 'toDisplayName' property of travel time segment");
		}

		// 'isActive' is optional
		try {
			active = properties.getBoolean("isActive");
		} catch (JSONException e) {
			MLog.INSTANCE.d(TAG,
					"Failed to parse 'isActive' property of travel time segment");
		}

		try {
			travelTimeMinutes = properties.getInt("travelTimeMinutes");
		} catch (JSONException e) {
			MLog.INSTANCE.d(TAG,
					"Failed to parse 'travelTimeMinutes' property of travel time segment");
		}

		includedInTotal = true;
	}

	@Override
	/**
	 * @return negative if 'this' is < 'another', positive if 'this' is > 'another', zero if 'this' is equal to 'another'
	 */
	public int compareTo(TravelTime another) {
		// Primary sort order is by first character in id
		// Secondary sort order is by remaining characters, with 'TOTAL'
		// appearing at end
		// eg. E1, E2, E3, ETOTAL, W1, W2, W3, WTOTAL
		int result = 0;

		String otherSegmentId = another.segmentId;
		int firstCharCompare = segmentId.substring(0, 1).compareTo(
				otherSegmentId.substring(0, 1));

		if (firstCharCompare == 0) {
			String thisEndChars = segmentId.substring(1);
			String otherEndChars = otherSegmentId.substring(1);
			boolean thisSecondCharIsNumber = isNumber(thisEndChars);
			boolean otherSecondCharIsNumber = isNumber(otherEndChars);

			if (thisSecondCharIsNumber && otherSecondCharIsNumber) {
				result = new Integer(thisEndChars).compareTo(new Integer(
						otherEndChars));
			} else if (thisSecondCharIsNumber && (!otherSecondCharIsNumber)) {
				result = -1;
			} else if ((!thisSecondCharIsNumber) && otherSecondCharIsNumber) {
				result = 1;
			}
		} else {
			result = firstCharCompare;
		}

		return result;
	}

	private boolean isNumber(String str) {
		boolean result = true;

		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			result = false;
		}

		return result;
	}

	public String getFromDisplayName() {
		return fromDisplayName;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public String getToDisplayName() {
		return toDisplayName;
	}

	public int getTravelTimeMinutes() {
		return travelTimeMinutes;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isIncludedInTotal() {
		return includedInTotal;
	}

	public boolean isTotal() {
		return segmentId == null ? false : segmentId.endsWith("TOTAL");
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setFromDisplayName(String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	public void setIncludedInTotal(boolean includedInTotal) {
		this.includedInTotal = includedInTotal;
		firePropertyChangeEvent(PROPERTY_INCLUDED_IN_TOTAL, includedInTotal);
	}
	
	public void setIncludedInTotalSilently(boolean includedInTotal) {
		this.includedInTotal = includedInTotal;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public void setToDisplayName(String toDisplayName) {
		this.toDisplayName = toDisplayName;
	}

	public void setTravelTimeMinutes(int travelTimeMinutes) {
		this.travelTimeMinutes = travelTimeMinutes;
		firePropertyChangeEvent(PROPERTY_TRAVEL_TIME_IN_MINUTES,
				travelTimeMinutes);
	}

}
