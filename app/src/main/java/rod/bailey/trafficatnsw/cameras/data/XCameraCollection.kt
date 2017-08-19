package rod.bailey.trafficatnsw.cameras.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * A file full of traffic cameras
 */
data class XCameraCollection(
	@SerializedName("features")
	val cameras: List<XCamera>?) {

	companion object {
		fun parseCameraJson(jsonString: String): XCameraCollection {
			return Gson().fromJson(jsonString, XCameraCollection::class.java)
		}
	}
}