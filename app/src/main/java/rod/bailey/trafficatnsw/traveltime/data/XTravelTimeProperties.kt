package rod.bailey.trafficatnsw.traveltime.data

import com.google.gson.annotations.SerializedName

/**
 * Created by rodbailey on 7/8/17.
 */
data class XTravelTimeProperties(

	@SerializedName("direction")
	val direction: String?,

	@SerializedName("toDisplayName")
	val toDisplayName: String? = null,

	@SerializedName("isActive")
	val isActive: Boolean? = false,

	@SerializedName("fromDisplayName")
	val fromDisplayName: String? = null) {

	@SerializedName("travelTimeMinutes")
	var travelTimeMinutes: Int? = 0
		get() = field
		set(travelTimeMinutes) {
			field = travelTimeMinutes
		}
}