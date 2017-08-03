package rod.bailey.trafficatnsw.hazard.data

import android.location.Location
import org.json.JSONException
import org.json.JSONObject
import rod.bailey.trafficatnsw.util.JSONUtils
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetJSONObject
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetString
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetBoolean
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetJSONArray
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetDate
import rod.bailey.trafficatnsw.util.JSONUtils.safeGetDoubleArrayElement

import rod.bailey.trafficatnsw.util.MLog
import java.util.*

class XHazard(json: JSONObject) : Comparable<XHazard> {
	val adviceA: String?
	val adviceB: String?
	val arrangementElements: LinkedList<XArrangementElement> = LinkedList<XArrangementElement>()
	val attendingGroups: LinkedList<String> = LinkedList<String>()
	val created: Date?
	val displayName: String?
	val end: Date?
	val isEnded: Boolean?
	val hazardId: Int?
	val headline: String?
	val isImpactingNetwork: Boolean?
	val isInitialReport: Boolean?
	val isMajor: Boolean?
	val lastUpdated: Date?
	val latlng: Location?
	val mainCategory: String?
	val otherAdvice: String?
	val periods: LinkedList<XPeriod> = LinkedList<XPeriod>()
	val properties: JSONObject?
	val publicTransport: String?
	val roads: LinkedList<XRoad> = LinkedList<XRoad>()
	val start: Date?
	val subCategoryA: String?
	val subCategoryB: String?
	val webLinkName: String?
	val webLinkUrl: String?

	init {
		properties = safeGetJSONObject(json, "properties")
		headline = safeGetString(properties, "headline")
		hazardId = JSONUtils.safeGetInt(json, "id")

		val geometryProperties = safeGetJSONObject(json, "geometry")
		val coordinates = safeGetJSONArray(geometryProperties,
			"coordinates")
		val firstCoord = safeGetDoubleArrayElement(coordinates, 0)
		val secondCoord = safeGetDoubleArrayElement(coordinates, 1)

		latlng = Location("")
		if ((firstCoord != null) && (secondCoord != null)) {
			latlng.latitude = secondCoord
			latlng.longitude = firstCoord
		}

		isInitialReport = safeGetBoolean(properties, "isInitialReport")
		isMajor = safeGetBoolean(properties, "isMajor")
		isEnded = safeGetBoolean(properties, "ended")
		isImpactingNetwork = safeGetBoolean(properties, "impactingNetwork")

		created = safeGetDate(properties, "created")
		start = safeGetDate(properties, "start")
		end = safeGetDate(properties, "end")
		lastUpdated = safeGetDate(properties, "lastUpdated")

		mainCategory = safeGetString(properties, "mainCategory")
		subCategoryA = safeGetString(properties, "subCategoryA")
		subCategoryB = safeGetString(properties, "subCategoryB")
		adviceA = safeGetString(properties, "adviceA")
		adviceB = safeGetString(properties, "adviceB")
		otherAdvice = safeGetString(properties, "otherAdvice")
		webLinkUrl = safeGetString(properties, "webLinkUrl")
		webLinkName = safeGetString(properties, "webLinkName")
		displayName = safeGetString(properties, "displayName")
		publicTransport = safeGetString(properties, "publicTransport")
	}

	// Parse "Attending groups"
	init {
		val attendingGroupsJSONArray = JSONUtils.safeGetJSONArray(
			properties, "attendingGroups")

		if (attendingGroupsJSONArray != null) {
			for (i in 0..attendingGroupsJSONArray.length() - 1) {
				val groupStr = JSONUtils.safeGetStringArrayElement(
					attendingGroupsJSONArray, i)
				if (groupStr != null) {
					attendingGroups.add(groupStr)
				}
			}
		}
	}

	// Parse ArrangementElements array
	init {
		val elementsJsonObjects = JSONUtils.safeGetJSONArray(properties,
			"arrangementElements")

		if (elementsJsonObjects != null) {
			for (i in 0..elementsJsonObjects.length() - 1) {

				val elementJsonObject = JSONUtils
					.safeGetJSONObjectArrayElement(elementsJsonObjects, i)
				if (elementJsonObject != null) {
					val element = XArrangementElement(
						elementJsonObject)
					arrangementElements.add(element)
				}
			}
		}

	}

	init {
		// Parse "periods" array
		val periodsJsonObjects = JSONUtils.safeGetJSONArray(properties,
			"periods")

		if (periodsJsonObjects != null) {
			for (i in 0..periodsJsonObjects.length() - 1) {
				val periodJsonObject = JSONUtils
					.safeGetJSONObjectArrayElement(periodsJsonObjects, i)

				if (periodJsonObject != null) {
					val period = XPeriod(periodJsonObject)
					periods.add(period)
				}
			}
		}
	}

	init {
		// Parse "roads" array
		val roadJsonObjects = JSONUtils.safeGetJSONArray(properties,
			"roads")

		if (roadJsonObjects != null) {
			for (j in 0..roadJsonObjects.length() - 1) {
				val roadJsonObject = JSONUtils
					.safeGetJSONObjectArrayElement(roadJsonObjects, j)

				if (roadJsonObject != null) {
					val road = XRoad(roadJsonObject)
					roads.add(road)
				}
			}
		}

	}

	/**
	 * @return a negative integer if this < another
	 * a positive integer if this > another
	 * 0 if this .euqals another
	 */
	override fun compareTo(other: XHazard): Int {
		val thisLastUpdate: Date = lastUpdated ?: Date()
		val otherLastUpdate: Date = other.lastUpdated ?: Date()
		return thisLastUpdate.compareTo(otherLastUpdate) * -1
	}

	companion object {

		private val TAG = XHazard::class.java.simpleName

		val TOKEN_ROAD_CLOSURE: String = "ROAD_CLOSURE"
		val TOKEN_LANE_CLOSURE: String = "LANE_CLOSURE"
		val TOKEN_EXTENT: String = "Affected"
		val TOKEN_EXTENT_BOTH: String = "Both directions"
		val TOKEN_LANES_CLOSED: String = "Lanes closed"
		val TOKEN_EXTENT_LANE_CLOSED: String = "Closed"

		fun parseIncidentJson(
			jsonContents: String?): List<XHazard> {
			val result = LinkedList<XHazard>()

			try {
				val topLevelObject = JSONObject(jsonContents)
				val features = topLevelObject.getJSONArray("features")

				MLog.d(TAG,
					   "Number of features in hazards JSON file: " + features.length())

				for (i in 0..features.length() - 1) {
					val featureJSONObject = features.getJSONObject(i)
					val hazard = XHazard(featureJSONObject)
					result.add(hazard)
				}
			} catch (e: JSONException) {
				MLog.e(TAG, "Failed to parse JSON in hazards file", e)
			}

			return result
		}
	}
}