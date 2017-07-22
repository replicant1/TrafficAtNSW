package rod.bailey.trafficatnsw.json.hazard

import org.json.JSONObject

import rod.bailey.trafficatnsw.util.JSONUtils.*

class XArrangementElement(elementJSONObject: JSONObject) {

	val title: String?
	val html: String?

	init {
		title = safeGetString(elementJSONObject, "title")
		html = safeGetString(elementJSONObject, "html")
	}
}
