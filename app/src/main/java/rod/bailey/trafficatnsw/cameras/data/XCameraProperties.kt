package rod.bailey.trafficatnsw.cameras.data

import com.google.gson.annotations.SerializedName

/**
 * The properties of a traffic camera
 */
data class XCameraProperties(

	/**
	 * String representation of a region. See [XRegion].
	 * e.g. SYD_SOUTH
	 */
	@SerializedName("region")
	val region: String?,

	/**
	 * e.g. 5 ways (Miranda), Alison Road (Randwick)
	 */
	@SerializedName("title")
	val title: String?,

	/**
	 * Describes the view the camera has e.g. "Alison Road at Darley oad looking north-west"
	 */
	@SerializedName("view")
	val view: String?,

	/**
	 * String rep of direction the camera is pointing. See [XCameraDirection]
	 */
	@SerializedName("direction")
	val direction: String?,

	/**
	 * URL to the latest image from this traffic camera. Updated every 60 seconds or so.
	 */
	@SerializedName("href")
	val imageURL: String?)