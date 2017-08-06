package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName

/**
 * A Hazard (incident) as represented in a JSON file from the live traffic
 * web site.
 */
data class XHazard(
	@SerializedName("id")
	val hazardId: Int?,

	@SerializedName("geometry")
	val geometry: XGeometry?,

	@SerializedName("properties")
	val properties: XProperties?) : Comparable<XHazard> {

	/**
	 * Hazards are sorted by [lastUpdated] date/time with the most recently
	 * updated appearing first.
	 *
	 * @return (1) a negative integer if this < another
	 * (2) a positive integer if this > another
	 * (3) 0 if this .equals another
	 */
	override fun compareTo(other: XHazard): Int {
		val thisLastUpdate: Long = properties?.lastUpdated ?: 0
		val otherLastUpdate: Long = other.properties?.lastUpdated ?: 0
		return thisLastUpdate.compareTo(otherLastUpdate) * -1
	}

	companion object {
		val TOKEN_ROAD_CLOSURE: String = "ROAD_CLOSURE"
		val TOKEN_LANE_CLOSURE: String = "LANE_CLOSURE"
		val TOKEN_EXTENT: String = "Affected"
		val TOKEN_EXTENT_BOTH: String = "Both directions"
		val TOKEN_LANES_CLOSED: String = "Lanes closed"
		val TOKEN_EXTENT_LANE_CLOSED: String = "Closed"
	}
}
