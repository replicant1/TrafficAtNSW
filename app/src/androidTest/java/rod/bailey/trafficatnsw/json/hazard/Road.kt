package rod.bailey.trafficatnsw.json.hazard

/**
 * The street/road/lane/motorway on which a hazard is occurring.
 */
data class Road(
	var conditionTendency: String? = null,
	var crossStreet: String? = null,
	var delay: String? = null,
	var fullStreetAddress: String? = null,
	var impactedLanes: List<Lane>? = null,
	var locationQualifier: String? = null,
	var mainStreet: String? = null,
	var queueLength: Int = 0, // Unit = km
	var region: String? = null, // SYD_NORTH, REG_SOUTH etc
	var secondLocation: String? = null,
	var suburb: String? = null,
	var trafficVolume: String? = null // "Heavy","Moderate" etc
)
