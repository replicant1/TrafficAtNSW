package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName

/**
 * Encapsulates an element of the "roads" property of a Hazard. There is
 * only ever one XRoad in practice, which is the road the hazard is
 * occurring on.
 */
data class XRoad(
	@SerializedName("conditionTendency")
	val conditionTendency: String?,

	@SerializedName("crossStreet")
	val crossStreet: String?,

	@SerializedName("delay")
	val delay: String?,

	@SerializedName("locationQualifier")
	val locationQualifier: String?,

	@SerializedName("mainStreet")
	val mainStreet: String?,

	@SerializedName("region")
	val region: String?,

	@SerializedName("secondLocation")
	val secondLocation: String?,

	@SerializedName("suburb")
	val suburb: String?,

	@SerializedName("trafficVolume")
	val trafficVolume: String?,

	@SerializedName("queueLength")
	val queueLength: Int?,

	@SerializedName("impactedLanes")
	val impactedLanes: List<XLane>?)


