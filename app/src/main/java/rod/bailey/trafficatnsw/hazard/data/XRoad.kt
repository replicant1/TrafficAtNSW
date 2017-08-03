package rod.bailey.trafficatnsw.hazard.data

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.util.LinkedList

import rod.bailey.trafficatnsw.util.JSONUtils
import rod.bailey.trafficatnsw.util.MLog

import rod.bailey.trafficatnsw.util.JSONUtils.safeGetString
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetInt
import rod.bailey.trafficatnsw.util.JSONUtils.isNonEmpty

class XRoad(json: JSONObject) {

	val conditionTendency: String?
	val crossStreet: String?
	val delay: String?
	val locationQualifier: String?
	val mainStreet: String?
	val region: String?
	val secondLocation: String?
	val suburb: String?
	val trafficVolume: String?
	val queueLength: Int?
	val fullStreetAddress: String?
	val impactedLanes: List<XLane>?

	init {
		conditionTendency = safeGetString(json, "conditionTendency")
		crossStreet = safeGetString(json, "crossStreet")
		delay = safeGetString(json, "delay")
		locationQualifier = safeGetString(json, "locationQualifier")
		mainStreet = safeGetString(json, "mainStreet")
		region = safeGetString(json, "region")
		secondLocation = safeGetString(json, "secondLocation")
		suburb = safeGetString(json, "suburb")
		trafficVolume = safeGetString(json, "trafficVolume")
		queueLength = safeGetInt(json, "queueLength")

		// The 'fullStreetAddress' is pieced together from these properties:
		// mainStreet, locationQualifier, crossStreet, secondLocation.
		// The only mandatory property is mainStreet

		val fullStr = StringBuffer(mainStreet)

		if (isNonEmpty(crossStreet)) {
			fullStr.append(" ")
			fullStr.append(locationQualifier)
			fullStr.append(" ")
			fullStr.append(crossStreet)
		}

		if (isNonEmpty(secondLocation)) {
			fullStr.append(" and ")
			fullStr.append(secondLocation)
		}

		fullStreetAddress = fullStr.toString()

		// Parse out the Lanes objects

		val lanes = LinkedList<XLane>()
		val lanesJSONArray:JSONArray? = JSONUtils.safeGetJSONArray(json, "impactedLanes")

		if (lanesJSONArray != null) {
			for (i in 0..lanesJSONArray.length() - 1) {
				try {
					val laneJsonObject = lanesJSONArray.getJSONObject(i)
					val lane = XLane(laneJsonObject)
					lanes.add(lane)
				}
				catch (e: JSONException) {
					MLog.w(TAG, "Failed to parse XLane from JSON object", e)
				}
			}
		}

		impactedLanes = lanes
	}

	companion object {

		private val TAG = XRoad::class.java.simpleName
	}

}
