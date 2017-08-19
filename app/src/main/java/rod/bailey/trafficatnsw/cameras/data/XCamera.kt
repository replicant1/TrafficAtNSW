package rod.bailey.trafficatnsw.cameras.data

import com.google.gson.annotations.SerializedName
import rod.bailey.trafficatnsw.hazard.data.XGeometry

/**
 * A traffic camera
 */
data class XCamera(
	@SerializedName("id")
	val id: String?,

	@SerializedName("geometry")
	val geometry: XGeometry?,

	@SerializedName("properties")
	val properties: XCameraProperties?)