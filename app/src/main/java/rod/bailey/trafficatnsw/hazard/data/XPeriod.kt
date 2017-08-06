package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.annotations.SerializedName

data class XPeriod(
	@SerializedName("closureType")
	val closureType: String?,

	@SerializedName("direction")
	val direction: String?,

	@SerializedName("finishTime")
	val finishTime: String?,

	@SerializedName("fromDay")
	val fromDay: String?,

	@SerializedName("startTime")
	val startTime: String?,

	@SerializedName("toDay")
	val toDay: String?)