package rod.bailey.trafficatnsw.json.hazard

import org.json.JSONObject

import rod.bailey.trafficatnsw.util.JSONUtils.*

class XPeriod(json: JSONObject) {

	val closureType: String?
	val direction: String?
	val finishTime: String?
	val fromDay: String?
	val startTime: String?
	val toDay: String?

	init {
		closureType = safeGetString(json, "closureType")
		direction = safeGetString(json, "direction")
		finishTime = safeGetString(json, "finishTime")
		fromDay = safeGetString(json, "fromDay")
		startTime = safeGetString(json, "startTime")
		toDay = safeGetString(json, "toDay")
	}

}
