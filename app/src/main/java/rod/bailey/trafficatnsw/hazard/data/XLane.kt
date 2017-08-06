package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

import rod.bailey.trafficatnsw.util.JSONUtils.safeGetString

class XLane(
	@SerializedName("affectedDirection")
	val affectedDirection: String?,

	@SerializedName("closedLanes")
	val closedLanes: String?,

	@SerializedName("description")
	val description: String?,

	@SerializedName("extent")
	val extent: String?,

	@SerializedName("numberOfLanes")
	val numberOfLanes: String?,

	@SerializedName("roadType")
	val roadType: String?)
