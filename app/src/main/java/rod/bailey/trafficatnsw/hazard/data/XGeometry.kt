package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName

/**
 * Created by rodbailey on 6/8/17.
 */
data class XGeometry(
	@SerializedName("coordinates")
	val latlng: DoubleArray?)