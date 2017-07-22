package rod.bailey.trafficatnsw.json.hazard

import com.google.gson.Gson

/**
 * Represents the entire JSON file containing multiple hazards.
 */
data class HazardCollection(var features: List<Hazard>? = null, var lastPublished: Long = 0L) {

	fun findHazardById(hazardId: Int): Hazard? {
		if (features != null) {
			for (hazard in features!!) {
				if (hazard.id == hazardId) {
					return hazard
				}
			}
		}
		return null
	}

	companion object {

		fun parseJson(jsonContents: String): HazardCollection {
			val gson = Gson()
			return gson.fromJson(jsonContents, HazardCollection::class.java)
		}
	}
}
