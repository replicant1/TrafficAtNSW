package rod.bailey.trafficatnsw.json.hazard

/**
 * Created by rodbailey on 11/10/2016.
 */
data class Lane(
	var affectedDirection: String? = null,
	var closedLanes: String? = null,
	var description: String? = null,
	var extent: String? = null,
	var numberOfLanes: String? = null,
	var roadType: String? = null
)
