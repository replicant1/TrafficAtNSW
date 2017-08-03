package rod.bailey.trafficatnsw.hazard.filter

import rod.bailey.trafficatnsw.hazard.data.XHazard
import rod.bailey.trafficatnsw.hazard.data.XRegion

class AdmitRegionalHazardFilter : IHazardFilter {
	override fun admit(hazard: XHazard): Boolean {
		var result = false
		val roads = hazard.roads
		if (!roads.isEmpty()) {
			val road = roads[0]
			val roadRegionStr:String? = road.region
			if (roadRegionStr != null) {
				val roadRegion: XRegion? = XRegion.valueOf(roadRegionStr)
				result = roadRegion?.isRegional ?: false
			}
		}

		return result
	}

	companion object {
		private val TAG = AdmitRegionalHazardFilter::class.java.simpleName
	}
}
