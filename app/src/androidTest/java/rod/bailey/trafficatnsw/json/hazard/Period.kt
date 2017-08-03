package rod.bailey.trafficatnsw.json.hazard

/**
 * A period of time over which a hazard will be present (usually an event)
 */
data class Period (
	var closureType: String? = null,
	var direction: String? = null,
	var finishTime: String? = null,
	var fromDay: String? = null,
	var startTime: String? = null,
	var toDay: String? = null
)
