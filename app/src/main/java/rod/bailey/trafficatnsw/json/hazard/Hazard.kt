package rod.bailey.trafficatnsw.json.hazard

/**
 * Anything that we list in this app - corresponds to the "Incident" type on
 * the Live Traffic web site.
 */
data class Hazard (
	var id: Int = 0,
	var geometry: HazardGeometry? = null,
	var properties: HazardProperties? = null
)
