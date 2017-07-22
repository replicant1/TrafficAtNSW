package rod.bailey.trafficatnsw.json.hazard

import android.location.Location

import org.json.JSONObject

import java.util.Date

/**
 * GSON domain class - represents a HazardProperties (incident).
 */
data class HazardProperties(
	var adviceA: String? = null,
	var adviceB: String? = null,
	var arrangementElements: List<ArrangementElement>? = null,
	var attendingGroups: List<String>? = null,
	var created: Long? = 0,
	var displayName: String? = null,
	var end: Long = 0,
	var ended: Boolean = false,
	var hazardId: Int? = 0,
	var headline: String? = null,
	var impactingNetwork: Boolean = false,
	var isInitialReport: Boolean = false,
	var isMajor: Boolean? = false,
	var lastUpdated: Long? = 0,
	var latlng: Location? = null,
	var mainCategory: String? = null,
	var otherAdvice: String? = null,
	var periods: List<Period>? = null,
	var properties: JSONObject? = null,
	var publicTransport: String? = null,
	var roads: List<Road>? = null,
	var start: Long? = 0,
	var subCategoryA: String? = null,
	var subCategoryB: String? = null,
	var webLinkName: String? = null,
	var webLinkUrl: String? = null) : Comparable<HazardProperties> {

	/**
	 * @return A negative integer if this < another. A positive integer if
	 * * this > another. 0 if this.equals() another,
	 */
	override fun compareTo(another: HazardProperties): Int {
		val thisUpdated: Long = lastUpdated ?: 0
		val otherUpdated: Long = another.lastUpdated ?: 0
		return (thisUpdated - otherUpdated).toInt()
	}

}
