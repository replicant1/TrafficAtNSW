package rod.bailey.trafficatnsw.json.hazard

/**
 * The street/road/lane/motorway on which a hazard is occurring.
 */
data class Road(
	var conditionTendency: String? = null,

	/** The nearest cross-street. Helps locate the hazard */
	var crossStreet: String? = null,

	/**
	 * Describes how much time delay the user can expect on their journey because of this
	 * hazard obstructing traffic.
	 */
	var delay: String? = null,
	var fullStreetAddress: String? = null,
	var impactedLanes: List<Lane>? = null,
	var locationQualifier: String? = null,

	/**
	 * Name of the "through" street which principally contains this hazard.
	 */
	var mainStreet: String? = null,

	/** Unit = kilometre */
	var queueLength: Int = 0,

	/**
	 * A token identifying the RMS region to which the hazard belongs eg. REG_SOUTH, SYD_NORTH
	 */
	var region: String? = null,
	var secondLocation: String? = null,

	/**
	 * Name of the suburb in which this Road/hazard is located eg: "Mount Colah", "Redfern" , "Sydney CBD"
	 */
	var suburb: String? = null,

	/** eg. "Heavy", "Moderate", "Light" */
	var trafficVolume: String? = null
)
