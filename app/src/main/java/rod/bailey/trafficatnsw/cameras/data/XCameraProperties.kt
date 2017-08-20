package rod.bailey.trafficatnsw.cameras.data

import com.google.gson.annotations.SerializedName

/**
 * Created by rodbailey on 20/8/17.
 */
data class XCameraProperties (
	@SerializedName("region")
	var region: String?,

	@SerializedName("title")
	val title: String?,

	@SerializedName("view")
	val view: String?,

	@SerializedName("direction")
	val direction: String?,

	@SerializedName("href")
	val imageURL: String?)