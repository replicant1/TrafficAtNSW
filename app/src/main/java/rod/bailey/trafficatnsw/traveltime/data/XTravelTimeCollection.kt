package rod.bailey.trafficatnsw.traveltime.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * A file full of travel times
 */
data class XTravelTimeCollection(

	@SerializedName("layerName")
	val layerName: String?,

	@SerializedName("features")
	val travelTimes: List<XTravelTimeSegment>) {

	companion object {
		fun parseTravelTimesJson(jsonString: String): XTravelTimeCollection {
			return Gson().fromJson(jsonString, XTravelTimeCollection::class.java)
		}
	}
}
