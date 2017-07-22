package rod.bailey.trafficatnsw.json.hazard

import org.json.JSONObject

import rod.bailey.trafficatnsw.util.JSONUtils.safeGetString

class XLane(json: JSONObject) {

	val affectedDirection: String?
	val closedLanes: String?
	val description: String?
	val extent: String?
	val numberOfLanes: String?
	val roadType: String?

	init {
		affectedDirection = safeGetString(json, "affectedDirection")
		closedLanes = safeGetString(json, "closedLanes")
		description = safeGetString(json, "description")
		extent = safeGetString(json, "extent")
		numberOfLanes = safeGetString(json, "numberOfLanes")
		roadType = safeGetString(json, "roadType")
	}
}
