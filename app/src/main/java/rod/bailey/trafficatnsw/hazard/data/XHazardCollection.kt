package rod.bailey.trafficatnsw.hazard.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * A file full of hazards (incidents)
 */
data class XHazardCollection(
	@SerializedName("layerName")
	val layerName: String?,

	@SerializedName("features")
	val hazards: List<XHazard>?) {

	companion object {
		fun parseIncidentJson(jsonString: String): XHazardCollection {
			return Gson().fromJson(jsonString, XHazardCollection::class.java)
		}
	}
}