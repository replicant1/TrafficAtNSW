package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName

/**
 * The geometry of a hazard is a pair of doubles, the first is latitude, the
 * seconds is longitude.
 */
data class XGeometry(
	@SerializedName("coordinates")
	val latlng: DoubleArray?)