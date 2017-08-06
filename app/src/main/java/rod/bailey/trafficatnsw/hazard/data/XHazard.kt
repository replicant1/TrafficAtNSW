package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class XHazard(
	@SerializedName("id")
	val hazardId: Int?,

	@SerializedName("geometry")
	val geometry: XGeometry,

	@SerializedName("properties")
	val properties: XProperties) : Comparable<XHazard> {

	/**
	 * @return a negative integer if this < another
	 * a positive integer if this > another
	 * 0 if this .equals another
	 */
	override fun compareTo(other: XHazard): Int {
		val thisLastUpdate: Date = properties.lastUpdated ?: Date()
		val otherLastUpdate: Date = other.properties.lastUpdated ?: Date()
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
